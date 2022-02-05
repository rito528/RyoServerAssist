package com.ryoserver.Home

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, Location}
import scalikejdbc.{AutoSession, DB, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID

object HomeData {

  private var homeData: Set[HomeDataType] = Set.empty

  def getTargetHomeData(uuid: UUID, point: Int): Option[HomeDataType] = {
    val data = homeData
      .filter(data => data.UUID == uuid && data.point == point)
    if (data.isEmpty) None
    else Option(data.head)
  }

  def swapHomeData(uuid: UUID, point: Int, data: HomeDataType): Unit = {
    removeHomeData(uuid, point)
    addHomeData(data)
  }

  private def addHomeData(data: HomeDataType): Unit = homeData += data

  private def removeHomeData(uuid: UUID, point: Int): Unit = {
    homeData = homeData
      .filterNot(data => data.UUID == uuid && data.point == point)
  }

  def loadHomeData(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    homeData = sql"SELECT * FROM Homes".map(rs => {
      val location = rs.string("Location").split(",")
      HomeDataType(
        UUID = UUID.fromString(rs.string("UUID")
          .replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5")),
        point = rs.int("point"),
        location = new Location(Bukkit.getWorld(location(0)), location(1).toDouble, location(2).toDouble, location(3).toDouble),
        isLocked = rs.boolean("Locked")
      )
    }).toList.apply().toSet
  }

  def saveHomeData(implicit ryoServerAssist: RyoServerAssist): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, oneMinute, oneMinute)
  }

  def save(): Unit = {
    DB.localTx(implicit session => {
      sql"DELETE FROM Homes;".execute.apply()
      homeData.foreach { data =>
        val locationString = s"${data.location.getWorld.getName},${data.location.getX.toInt},${data.location.getY.toInt},${data.location.getZ.toInt}"
        sql"INSERT INTO Homes (UUID,point,Location,Locked) VALUES (${data.UUID.toString},${data.point},$locationString,${data.isLocked})".execute.apply()
      }
    })
  }

}

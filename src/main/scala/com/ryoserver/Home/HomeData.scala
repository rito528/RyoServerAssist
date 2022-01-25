package com.ryoserver.Home

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, Location}

import java.util.UUID

object HomeData {

  private var homeData: Set[HomeDataType] = Set.empty

  def getTargetHomeData(uuid: UUID,point: Int): Option[HomeDataType]= {
    val data = homeData
      .filter(data => data.UUID == uuid && data.point == point)
    if (data.isEmpty) None
    else Option(data.head)
  }

  private def addHomeData(data: HomeDataType): Unit = homeData += data

  private def removeHomeData(uuid: UUID,point: Int): Unit = {
    homeData = homeData
      .filterNot(data => data.UUID == uuid && data.point == point)
  }

  def swapHomeData(uuid: UUID,point: Int,data:HomeDataType): Unit = {
    removeHomeData(uuid,point)
    addHomeData(data)
  }

  def loadHomeData(): Unit = {
    val sql = new SQL
    val rs = sql.executeQuery("SELECT * FROM Homes;")
    homeData = Iterator.from(0)
      .takeWhile(_ => rs.next())
      .map(_ => {
        val location = rs.getString("Location").split(",")
        HomeDataType(
          UUID = UUID.fromString(rs.getString("UUID")
          .replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5")),
          point = rs.getInt("point"),
          location = new Location(Bukkit.getWorld(location(0)),location(1).toDouble,location(2).toDouble,location(3).toDouble),
          isLocked = rs.getBoolean("Locked")
        )
      }).toSet
    sql.close()
  }

  def save(): Unit = {
    val sql = new SQL
    sql.executeSQL("DELETE FROM Homes;")
    homeData.foreach{data =>
      val locationString = s"${data.location.getWorld},${data.location.getX},${data.location.getY},${data.location.getZ}"
      sql.executeSQL(s"INSERT INTO Homes (UUID,point,Location,Locked) VALUES (${data.UUID.toString},${data.point},$data,$locationString)")
    }
    sql.close()
  }

  def saveHomeData(ryoServerAssist: RyoServerAssist): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
  }

}

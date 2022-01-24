package com.ryoserver.Home

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.{Bukkit, Location}

import java.util.UUID

object HomeData {

  private var homeData: Set[HomeDataType] = Set.empty

  def loadData(): Unit = {
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
  }

}

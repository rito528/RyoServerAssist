package com.ryoserver.Profile

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class ProfileGateway(ryoServerAssist: RyoServerAssist) {

  def setProfile(profileName: String, contents: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET $profileName='$contents';")
    sql.close()
  }

  def getProfile(uuid: String): Map[String, String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT Twitter,Discord,Word FROM Players WHERE UUID='$uuid';")
    if (rs.next()) {
      val data = Map(
        "Twitter" -> {
          if (rs.getString("Twitter").isEmpty) "" else rs.getString("Twitter")
        },
        "Discord" -> {
          if (rs.getString("Discord").isEmpty) "" else rs.getString("Discord")
        },
        "Word" -> {
          if (rs.getString("Word").isEmpty) "" else rs.getString("Word")
        },
      )
      sql.close()
      data
    } else {
      sql.close()
      null
    }
  }

}

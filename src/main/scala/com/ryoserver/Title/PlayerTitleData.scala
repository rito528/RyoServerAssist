package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class PlayerTitleData(ryoServerAssist: RyoServerAssist) {

  def getHasTitles(uuid:String): Array[String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT OpenedTitles FROM Players WHERE UUID='${uuid}';")
    var titles = Array.empty[String]
    if (rs.next() && rs.getString("OpenedTitles") != null) titles = rs.getString("OpenedTitles").split(";")

    sql.close()
    titles
  }

  def hasTitle(uuid:String,title:String): Boolean = getHasTitles(uuid).contains(title)

  def openTitle(uuid:String,title:String): Boolean = {
    if (hasTitle(uuid,title)) return false
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET OpenedTitles='${getHasTitles(uuid).mkString(";") + ";" + title};' WHERE UUID='$uuid'")
    sql.close()
    true
  }

  def removeTitle(uuid:String,title:String): Boolean = {
    if (!hasTitle(uuid,title)) return false
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET OpenedTitles='${getHasTitles(uuid).filterNot(_ == title).mkString(";") + ";"}'")
    sql.close()
    true
  }

}

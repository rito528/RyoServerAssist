package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class PlayerTitleData(ryoServerAssist: RyoServerAssist) {

  def getHasTitles(uuid: String): Array[String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT OpenedTitles FROM Players WHERE UUID='$uuid';")
    var titles = Array.empty[String]
    if (rs.next() && rs.getString("OpenedTitles") != null) titles = rs.getString("OpenedTitles").split(";")

    sql.close()
    titles
  }

  def hasTitle(uuid: String, title: String): Boolean = getHasTitles(uuid).contains(title)

  def openTitle(p: Player, title: String): Boolean = {
    val uuid = p.getUniqueId.toString
    if (hasTitle(uuid, title)) return false
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET OpenedTitles='${getHasTitles(uuid).mkString(";") + (if (getHasTitles(uuid).mkString(";") != "") ";" else "") + title};' WHERE UUID='$uuid'")
    sql.close()
    new GiveTitle(ryoServerAssist).titleGetNumber(p)
    true
  }

  def removeTitle(uuid: String, title: String): Boolean = {
    if (!hasTitle(uuid, title)) return false
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET OpenedTitles='${getHasTitles(uuid).filterNot(_ == title).mkString(";") + ";"}' WHERE UUID='$uuid'")
    sql.close()
    true
  }

  def getSelectedTitle(uuid: String): String = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT selectedTitle FROM Players WHERE UUID='$uuid';")
    var title: String = null
    if (rs.next()) title = rs.getString("selectedTitle")
    sql.close()
    title
  }

  def setSelectTitle(uuid: String, title: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET selectedTitle='$title' WHERE UUID='$uuid';")
    sql.close()
  }

  def resetSelectTitle(uuid: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET selectedTitle=NULL WHERE UUID='$uuid';")
    sql.close()
  }

}

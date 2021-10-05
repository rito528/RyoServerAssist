package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class PlayerTitleData(ryoServerAssist: RyoServerAssist) {

  def getHasTitles(p:Player): Array[String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT OpenedTitles FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    var titles = Array.empty[String]
    titles = rs.getString("OpenedTitles").split(";")
    sql.close()
    titles
  }

  def hasTitle(p:Player,title:String): Boolean = getHasTitles(p).contains(title)

  def openTitle(p:Player,title:String): Boolean = {
    if (hasTitle(p,title)) return false
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET OpenTitles='${getHasTitles(p).mkString(";") + title};' WHERE UUID=${p.getUniqueId.toString}")
    sql.close()
    true
  }

}

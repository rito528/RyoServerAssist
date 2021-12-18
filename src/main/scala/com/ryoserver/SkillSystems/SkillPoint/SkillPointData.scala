package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class SkillPointData(ryoServerAssist: RyoServerAssist) {

  def getSkillPoint(p: Player): Double = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT SkillPoint FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var sp = 0.0
    if (rs.next()) {
      sp = rs.getDouble("SkillPoint")
    }
    sql.close()
    sp
  }

  def setSkillPoint(p: Player, point: Double): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET SkillPoint='${point}' WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
    SkillPointBer.update(p, ryoServerAssist)
  }

}

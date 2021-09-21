package com.ryoserver.Skill.SkillPoint

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class SkillPointData(ryoServerAssist: RyoServerAssist) {

  def getSkillPoint(p:Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT SkillPoint FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var sp = 0
    if (rs.next()) {
      sp = rs.getInt("SkillPoint")
      sql.close()
    }
    sp
  }

}

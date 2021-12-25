package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class SkillOpenData(ryoServerAssist: RyoServerAssist) {

  def getSkillOpenPoint(p: Player): Int = Data.playerData(p.getUniqueId).SkillOpenPoint

  def openSkill(p: Player, skillName: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    val alreadyOpenedSkill = getOpenedSkill(p).mkString(",")
    sql.executeSQL(s"UPDATE Players SET OpenedSkills='${alreadyOpenedSkill + (if (alreadyOpenedSkill != "") "," else "") + SkillNames.indexOf(skillName)}',SkillOpenPoint=SkillOpenPoint - 10" +
      s" WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
    new GiveTitle(ryoServerAssist).skillOpenNumber(p)
  }

  def getOpenedSkill(p: Player): Array[String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT OpenedSkills FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var openedSkills: Array[String] = Array.empty
    if (rs.next()) {
      val skills = rs.getString("OpenedSkills")
      if (skills != null) {
        openedSkills = skills.split(",")
      }
    }
    sql.close()
    openedSkills
  }

  def addSkillOpenPoint(p: Player, point: Int): Unit = {
    new RyoServerPlayer(p).addSkillOpenPoint(point)
  }

  def addOpenSpecialSkillPoint(p: Player, point: Int): Unit = {
    new RyoServerPlayer(p).addSpecialSkillOpenPoint(point)
  }

}

package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.Player.{Data, PlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class SkillOpenData(ryoServerAssist: RyoServerAssist) {

  def getSkillOpenPoint(p: Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT SkillOpenPoint FROM Players WHERE UUID='${p.getUniqueId}'")
    var pt = 0
    if (rs.next()) pt = rs.getInt("SkillOpenPoint")
    sql.close()
    pt
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

  def openSkill(p: Player, skillName: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    val alreadyOpenedSkill = getOpenedSkill(p).mkString(",")
    sql.executeSQL(s"UPDATE Players SET OpenedSkills='${alreadyOpenedSkill + (if (alreadyOpenedSkill != "") "," else "") + SkillNames.indexOf(skillName)}',SkillOpenPoint=SkillOpenPoint - 10" +
      s" WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
    new GiveTitle(ryoServerAssist).skillOpenNumber(p)
  }

  def addSkillOpenPoint(p: Player, point: Int): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET SkillOpenPoint=SkillOpenPoint + $point WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
  }

  def addOpenSpecialSkillPoint(p:Player,addPoint:Int): Unit = {
    val oldPlayerData = Data.playerData(p.getUniqueId.toString)
    Data.playerData = Data.playerData.filterNot{case (uuid,_) => uuid == p.getUniqueId.toString}
    Data.playerData += (p.getUniqueId.toString -> oldPlayerData.copy(specialSkillOpenPoint = oldPlayerData.specialSkillOpenPoint + addPoint))
  }

}

package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import com.ryoserver.Title.GiveTitle
import org.bukkit.entity.Player

class SkillOpenData(ryoServerAssist: RyoServerAssist) {

  def getSkillOpenPoint(p: Player): Int = Data.playerData(p.getUniqueId).SkillOpenPoint

  def openSkill(p: Player, skillName: String): Unit = {
    val alreadyOpenedSkill = getOpenedSkill(p).mkString(",")
    val rp = new RyoServerPlayer(p)
    rp.addSkillOpenPoint(-10)
    rp.skillOpen(alreadyOpenedSkill + (if (alreadyOpenedSkill != "") "," else "") + SkillNames.indexOf(skillName))
    new GiveTitle(ryoServerAssist).skillOpenNumber(p)
  }

  def getOpenedSkill(p: Player): Array[String] = {
    Data.playerData(p.getUniqueId).OpenedSkills match {
      case Some(skills) =>
        skills.split(",")
      case None =>
        Array.empty
    }
  }

  def addOpenSpecialSkillPoint(p: Player, point: Int): Unit = {
    new RyoServerPlayer(p).addSpecialSkillOpenPoint(point)
  }

}

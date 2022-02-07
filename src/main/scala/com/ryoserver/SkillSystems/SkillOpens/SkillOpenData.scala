package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.Title.GiveTitle
import org.bukkit.entity.Player

class SkillOpenData {

  def openSkill(p: Player, skillName: String): Unit = {
    val alreadyOpenedSkill = getOpenedSkill(p).mkString(",")
    p.addSkillOpenPoint(-10)
    p.openSkills(alreadyOpenedSkill + (if (alreadyOpenedSkill != "") "," else "") + SkillNames.indexOf(skillName))
    new GiveTitle().skillOpenNumber(p)
  }

  def getOpenedSkill(p: Player): Array[String] = {
    p.getOpenedSkills match {
      case Some(skills) =>
        skills.split(",")
      case None =>
        Array.empty
    }
  }

  def addOpenSpecialSkillPoint(p: Player, point: Int): Unit = {
    p.addSpecialSkillOpenPoint(point)
  }

}

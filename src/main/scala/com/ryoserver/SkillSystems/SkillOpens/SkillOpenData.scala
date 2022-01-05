package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.Player.{PlayerData, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import com.ryoserver.Title.GiveTitle
import org.bukkit.entity.Player

class SkillOpenData(ryoServerAssist: RyoServerAssist) {

  def openSkill(p: Player, skillName: String): Unit = {
    val alreadyOpenedSkill = getOpenedSkill(p).mkString(",")
    p.addSkillOpenPoint(-10)
    p.openSkills(alreadyOpenedSkill + (if (alreadyOpenedSkill != "") "," else "") + SkillNames.indexOf(skillName))
    new GiveTitle(ryoServerAssist).skillOpenNumber(p)
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

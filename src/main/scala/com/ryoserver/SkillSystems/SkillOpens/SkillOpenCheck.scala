package com.ryoserver.SkillSystems.SkillOpens

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import org.bukkit.entity.Player

class SkillOpenCheck(ryoServerAssist: RyoServerAssist) {

  def isTrueOpen(skillName: String, p: Player): Boolean = {
    val data = new SkillOpenData(ryoServerAssist)
    val getPoint = data.getSkillOpenPoint(p)
    val skillID = SkillData.SkillNames.indexOf(skillName)
    if (getPoint < 10) return false

    if (skillID >= 6) {
      //ID6以上のスキルは基本スキルをすべて開放している必要がある
      var check = true
      for (i <- 0 to 5) {
        if (!isOpened(SkillData.SkillNames(i), p)) check = false
      }
      check
    } else {
      true
    }

  }

  def isOpened(skillName: String, p: Player): Boolean = {
    val data = new SkillOpenData(ryoServerAssist)
    val openedSkills = data.getOpenedSkill(p)
    openedSkills.contains(SkillNames.indexOf(skillName).toString)
  }

}

package com.ryoserver.SkillSystems.Skill.EffectSkill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.PlayerSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillMenu.SelectSkillMenu
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import com.ryoserver.SkillSystems.SkillPoint.SkillPointData
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

trait SkillToggle {

  val p: Player
  val ryoServerAssist: RyoServerAssist

  def effect(effectType: PotionEffectType, level: Int, sp: Int, skillName: String): Unit = {
    val openCheck = new SkillOpenCheck(ryoServerAssist)
    if (!openCheck.isOpened(skillName, p) && !openCheck.isTrueOpen(skillName, p)) {
      p.sendMessage(RED + "このスキルは開放できません！")
      return
    } else if (openCheck.isTrueOpen(skillName, p) && !openCheck.isOpened(skillName, p)) {
      new SkillOpenData(ryoServerAssist).openSkill(p, skillName)
      p.sendMessage(AQUA + "スキル:" + skillName + "を開放しました！")
      new SelectSkillMenu(ryoServerAssist).openMenu(p)
      return
    }
    if (new SkillPointData().getSkillPoint(p) < sp) {
      allEffectClear(p)
      p.sendMessage(RED + "スキルポイントが足りないためスキルを起動できませんでした！")
      return
    }
    val operation = new SkillOperation(p, skillName, ryoServerAssist)
    if (operation.isEnableSkill) {
      //スキルを無効化する
      operation.skillInvalidation()
      p.sendMessage(AQUA + "スキルを無効化しました。")
    } else {
      //スキルの有効化
      operation.skillActivation(effectType, level, sp)
      p.sendMessage(AQUA + "スキル:" + skillName + "を有効化しました。")
    }
  }

  def allEffectClear(p: Player): Unit = {
    if (enableSkills.contains(p.getName)) {
      enableSkills(p.getName).foreach(skillName => {
        new SkillOperation(p, skillName, ryoServerAssist).skillInvalidation()
      })
    }
  }

}

class skillToggleClass(val p: Player, val ryoServerAssist: RyoServerAssist) extends SkillToggle {}

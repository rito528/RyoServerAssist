package com.ryoserver.SkillSystems.Skill.EffectSkill

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.PlayerSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillMenu.SelectSkillMenu
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

trait SkillToggle {

  val p: Player
  val ryoServerAssist: RyoServerAssist

  def effect(effectType: PotionEffectType, level: Int, sp: Int, skillName: String): Unit = {
    val openCheck = new SkillOpenCheck()
    if (!openCheck.isOpened(skillName, p) && !openCheck.isTrueOpen(skillName, p)) {
      p.sendMessage(s"${RED}このスキルは開放できません！")
      return
    } else if (openCheck.isTrueOpen(skillName, p) && !openCheck.isOpened(skillName, p)) {
      new SkillOpenData().openSkill(p, skillName)
      p.sendMessage(s"${AQUA}スキル:${skillName}を開放しました！")
      new SelectSkillMenu(ryoServerAssist).openMenu(p)
      return
    }
    if (p.getSkillPoint < sp) {
      allEffectClear(p)
      p.sendMessage(s"${RED}スキルポイントが足りないためスキルを起動できませんでした！")
      return
    }
    val operation = new SkillOperation(p, skillName, ryoServerAssist)
    if (operation.isEnableSkill) {
      //スキルを無効化する
      operation.skillInvalidation()
      p.sendMessage(s"${AQUA}スキルを無効化しました。")
    } else {
      //スキルの有効化
      operation.skillActivation(effectType, level, sp)
      p.sendMessage(s"${AQUA}スキル:${skillName}を有効化しました。")
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

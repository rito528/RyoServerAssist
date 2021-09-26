package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.PlayerSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import com.ryoserver.SkillSystems.SkillPoint.SkillPointData
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

trait SkillToggle {

  val p:Player
  val ryoServerAssist:RyoServerAssist

  def effect(effectType:PotionEffectType,level:Int,sp:Int,skillName:String): Unit = {
    val openCheck = new SkillOpenCheck(ryoServerAssist)
    if (!openCheck.isOpened(skillName,p) && !openCheck.isTrueOpen(skillName,p)) {
      p.sendMessage(ChatColor.RED + "このスキルは開放できません！")
      return
    } else if (openCheck.isTrueOpen(skillName,p) && !openCheck.isOpened(skillName,p)) {
      new SkillOpenData(ryoServerAssist).openSkill(p, skillName)
      p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "を開放しました！")
      new SelectSkillMenu(ryoServerAssist).openMenu(p)
      return
    }
    if (new SkillPointData(ryoServerAssist).getSkillPoint(p) < sp) {
      allEffectClear(p)
      p.sendMessage(ChatColor.RED + "スキルポイントが足りないためスキルを起動できませんでした！")
      return
    }
    val operation = new SkillOperation(p,skillName,ryoServerAssist)
    if (operation.isEnableSkill) {
      //スキルを無効化する
      operation.skillInvalidation()
      p.sendMessage(ChatColor.AQUA + "スキルを無効化しました。")
    } else {
      //スキルの有効化
      operation.skillActivation(effectType,level,sp)
      p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "を有効化しました。")
    }
  }

  def allEffectClear(p:Player): Unit = {
    enableSkills(p.getName).foreach(skillName => {
      new SkillOperation(p,skillName,ryoServerAssist).skillInvalidation()
    })
    p.sendMessage(ChatColor.AQUA + "スキルをすべて無効化しました。")
  }

}

class skillToggleClass(val p:Player, val ryoServerAssist: RyoServerAssist) extends SkillToggle {}

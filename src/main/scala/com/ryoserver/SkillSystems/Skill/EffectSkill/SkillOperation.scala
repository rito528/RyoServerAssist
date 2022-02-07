package com.ryoserver.SkillSystems.Skill.EffectSkill

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

class SkillOperation(ryoServerAssist: RyoServerAssist) {

  def skillActivation(p: Player,effectSkills: EffectSkills): Unit = {
    if (p.getSkillOpenPoint < 10 && !p.getOpenedSkills.contains(effectSkills)) {
      p.sendMessage(s"${RED}エフェクトスキル:${effectSkills.skillName}を開放していないため、有効にできません！")
      return
    } else if (p.getSkillOpenPoint >= 10 && !p.getOpenedSkills.contains(effectSkills)) {
      p.sendMessage(s"${AQUA}エフェクトスキル:${effectSkills.skillName}を開放しました！")
      return
    } else if (p.getSkillPoint < effectSkills.cost && EffectSkillData.getEnablingSkill(p) != effectSkills) {
      p.sendMessage(s"${RED}スキルポイントが足りません！")
      return
    }
    new BukkitRunnable {
      override def run(): Unit = {
        if (EffectSkillData.getEnablingSkill(p) != effectSkills) this.cancel()
        else p.addPotionEffect(new PotionEffect(effectSkills.effectType, 280, effectSkills.effectLevel))
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20)

    new BukkitRunnable {
      override def run(): Unit = {
        if (p.getSkillPoint < effectSkills.cost) {
          EffectSkillData.setDisableSkill(p)
          this.cancel()
          p.sendMessage(s"${RED}スキルポイントが不足したため、スキルを無効化しました。")
        } else {
          new SkillPointConsumption().consumption(effectSkills.cost, p)
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20 * 60)
  }

}

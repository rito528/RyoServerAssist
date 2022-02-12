package com.ryoserver.SkillSystems.Skill.EffectSkill

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.scheduler.BukkitRunnable

class SkillOperation(ryoServerAssist: RyoServerAssist) {

  def skillActivation(p: Player, effectSkills: EffectSkills): Unit = {
    if (p.getSkillOpenPoint < 10 && !p.getOpenedSkills.contains(effectSkills)) {
      p.sendMessage(s"${RED}エフェクトスキル:${effectSkills.skillName}を開放していないため、有効にできません！")
      return
    } else if (p.getSkillOpenPoint >= 10 && !p.getOpenedSkills.contains(effectSkills)) {
      p.openSkills(effectSkills)
      p.addSkillOpenPoint(-10)
      new GiveTitle().skillOpenNumber(p)
      p.sendMessage(s"${AQUA}エフェクトスキル:${effectSkills.skillName}を開放しました！")
      return
    } else if (p.getSkillPoint < effectSkills.cost && !EffectSkillData.getEnablingSkill(p).contains(effectSkills)) {
      p.sendMessage(s"${RED}スキルポイントが足りないため、${effectSkills.skillName}を有効化できませんでした。")
      return
    } else if (EffectSkillData.getEnablingSkill(p).contains(effectSkills)) {
      p.sendMessage(s"${AQUA}エフェクトスキル:${effectSkills.skillName}を無効化しました。")
      EffectSkillData.setDisableSkill(p, effectSkills)
      return
    }
    EffectSkillData.setEnablingSkill(p, effectSkills)
    new BukkitRunnable {
      override def run(): Unit = {
        new BukkitRunnable {
          override def run(): Unit = {
            if (!EffectSkillData.getEnablingSkill(p).contains(effectSkills)) this.cancel()
            else p.addPotionEffect(new PotionEffect(effectSkills.effectType, 280, effectSkills.effectLevel))
          }
        }.runTask(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20)

    new BukkitRunnable {
      override def run(): Unit = {
        if (p.getSkillPoint < effectSkills.cost) {
          EffectSkillData.setDisableSkill(p, effectSkills)
          this.cancel()
          p.sendMessage(s"${RED}スキルポイントが不足したため、エフェクトスキル:${effectSkills.skillName}を無効化しました。")
        } else {
          if (EffectSkillData.getEnablingSkill(p).contains(effectSkills)) {
            new SkillPointConsumption().consumption(effectSkills.cost, p)
          } else {
            this.cancel()
          }
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20 * 60)
    p.sendMessage(s"${AQUA}エフェクトスキル:${effectSkills.skillName}を有効化しました。")
  }

  def allDisablingSkills(p: Player): Unit = {
    EffectSkillData.getEnablingSkill(p).foreach(EffectSkillData.setDisableSkill(p, _))
    p.sendMessage(s"${AQUA}エフェクトスキルをすべて無効化しました。")
  }

}

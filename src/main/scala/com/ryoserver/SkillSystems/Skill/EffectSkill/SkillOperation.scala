package com.ryoserver.SkillSystems.Skill.EffectSkill

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.PlayerSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

class SkillOperation(p: Player, skillName: String, ryoServerAssist: RyoServerAssist) {

  def skillActivation(skillEffect: PotionEffectType, level: Int, sp: Int): Unit = {
    val name = p.getName
    if (enableSkills.contains(name)) {
      enableSkills(name) :+= skillName
    } else {
      var skillList: Array[String] = Array.empty
      skillList :+= skillName
      enableSkills += (name -> skillList)
    }
    var runnable: BukkitRunnable = null
    //エフェクトをつける
    new BukkitRunnable {
      override def run(): Unit = {
        runnable = this
        new BukkitRunnable {
          override def run(): Unit = {
            if (!isEnableSkill) runnable.cancel()
            else p.addPotionEffect(new PotionEffect(skillEffect, 280, level))
          }
        }.runTask(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20)

    new BukkitRunnable {
      override def run(): Unit = {
        //60秒ごとにスキルポイントを更新
        if (!isEnableSkill) {
          runnable.cancel()
          this.cancel()
          skillInvalidation()
        } else if (p.getSkillPoint < sp) {
          runnable.cancel()
          this.cancel()
          skillInvalidation()
          p.sendMessage(s"${RED}スキルポイントが不足したため、スキルを無効化しました。")
        } else {
          new SkillPointConsumption().consumption(sp, p)
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 20 * 60)
  }

  def isEnableSkill: Boolean = {
    val name = p.getName
    if (!enableSkills.contains(name)) return false
    if (!enableSkills(name).contains(skillName)) return false
    true
  }

  def skillInvalidation(): Unit = {
    val name = p.getName
    enableSkills(name) = enableSkills(name).filterNot(_ == skillName)
  }

}

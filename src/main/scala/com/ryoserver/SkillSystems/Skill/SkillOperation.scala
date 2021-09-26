package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.PlayerSkillData.enableSkills
import com.ryoserver.SkillSystems.SkillPoint.{SkillPointData, skillPointConsumption}
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

class SkillOperation(p:Player,skillName:String,ryoServerAssist: RyoServerAssist) {

  def isEnableSkill: Boolean = {
    val name = p.getName
    if (!enableSkills.contains(name)) return false
    if (!enableSkills(name).contains(skillName)) return false
    true
  }

  def skillActivation(skillEffect:PotionEffectType,level:Int,sp:Int): Unit = {
    val name = p.getName
    if (enableSkills.contains(name)) {
      enableSkills(name) :+= skillName
    } else {
      var skillList:Array[String] = Array.empty
      skillList :+= skillName
      enableSkills += (name -> skillList)
    }
    new BukkitRunnable {
      override def run(): Unit = {
        //60秒ごとにスキルポイントを更新
        if (!isEnableSkill) {
          this.cancel()
          skillInvalidation()
        } else {
          new skillPointConsumption(ryoServerAssist).consumption(sp, p)
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,20*60)

    //エフェクトをつける
    new BukkitRunnable {
      override def run(): Unit = {
        val runnable = this
        new BukkitRunnable {
          override def run(): Unit = {
            if (!isEnableSkill) {
              runnable.cancel()
              skillInvalidation()
              p.sendMessage(ChatColor.AQUA + "スキルを無効化しました。")
            } else if (new SkillPointData(ryoServerAssist).getSkillPoint(p) < sp) {
              runnable.cancel()
              skillInvalidation()
              p.sendMessage(ChatColor.DARK_RED + "スキルポイントが不足したため、スキルを無効化しました。")
            } else {
              p.addPotionEffect(new PotionEffect(skillEffect,40, level))
            }
          }
        }.runTask(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,20)
  }

  def skillInvalidation(): Unit = {
    val name = p.getName
    enableSkills(name) = enableSkills(name).filterNot(_ == skillName)
  }

}

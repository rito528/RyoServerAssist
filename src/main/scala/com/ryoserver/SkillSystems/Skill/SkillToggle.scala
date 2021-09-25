package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.PlayerSkillData.skillMap
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import com.ryoserver.SkillSystems.SkillPoint.skillPointConsumption
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

trait SkillToggle {

  val p:Player
  val ryoServerAssist:RyoServerAssist

  def effect(effectType:PotionEffectType,level:Int,sp:Int,skillName:String): Unit = {
    val openCheck = new SkillOpenCheck(ryoServerAssist)
    if (!openCheck.isOpened(skillName,p) && !openCheck.isTrueOpen(skillName,p)) {
      p.sendMessage(ChatColor.RED + "このスキルは開放できません！")
      return
    } else if (openCheck.isTrueOpen(skillName,p) && !openCheck.isOpened(skillName,p)) {
      new SkillOpenData(ryoServerAssist).openSkill(p,skillName)
      p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "を開放しました！")
      new SelectSkillMenu(ryoServerAssist).openMenu(p)
      return
    }
    if (skillMap.contains(p.getName) && skillMap(p.getName).contains(skillName)) {
      p.removePotionEffect(effectType)
      skillMap(p.getName)(skillName).cancel()
      val runnableMap = skillMap(p.getName).filterNot{case (name,_) => name == skillName}
      skillMap += (p.getName -> runnableMap)
      p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "を無効化しました。")
    } else {
      new BukkitRunnable {
        override def run(): Unit = {
          new BukkitRunnable {
            override def run(): Unit = {
              p.addPotionEffect(new PotionEffect(effectType,20*60,level))
            }
          }.runTask(ryoServerAssist)
          new skillPointConsumption(ryoServerAssist).consumption(sp,p)
          if (!skillMap.contains(p.getName)) skillMap += (p.getName -> mutable.Map(skillName -> this))
          else skillMap(p.getName) += (skillName -> this)
        }
      }.runTaskTimerAsynchronously(ryoServerAssist,0,20*60)
      p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "を有効化しました。")
    }
  }

  def allEffectClear(p:Player): Unit = {
    if (skillMap.contains(p.getName)) {
      p.getActivePotionEffects.forEach(effect => {
        p.removePotionEffect(effect.getType)
      })
      skillMap(p.getName).foreach { case (skillName, _) => {
        skillMap(p.getName)(skillName).cancel()
        val runnableMap = skillMap(p.getName).filterNot { case (name, _) => name == skillName }
        skillMap += (p.getName -> runnableMap)
      }
      }
      p.sendMessage(ChatColor.AQUA + "スキルをすべて無効化しました。")
    } else {
      p.sendMessage(ChatColor.RED + "スキルが有効化されていないため、無効化できませんでした。")
    }
  }

}

class skillToggleClass(val p:Player, val ryoServerAssist: RyoServerAssist) extends SkillToggle {}

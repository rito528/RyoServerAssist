package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.skillPointConsumption
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

object useSkill {

  var skillMap:mutable.Map[String,mutable.Map[String,BukkitRunnable]] = mutable.Map.empty

  private def effect(p:Player,effectType:PotionEffectType,level:Int,ryoServerAssist: RyoServerAssist,sp:Int,skillName:String): Unit = {
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

  def resistance(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.DAMAGE_RESISTANCE,0,ryoServerAssist,300,"耐性")
  }

  def speed(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.SPEED,0,ryoServerAssist,300,"移動速度上昇")
  }

  def jump(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.JUMP,0,ryoServerAssist,300,"跳躍力上昇")
  }

  def damageUp(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.INCREASE_DAMAGE,0,ryoServerAssist,300,"攻撃力上昇")
  }

  def diggingUp(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.FAST_DIGGING,0,ryoServerAssist,300,"採掘速度上昇")
  }

  def regeneration(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.REGENERATION,0,ryoServerAssist,300,"再生能力")
  }

  def slowFalling(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.SLOW_FALLING,0,ryoServerAssist,600,"低速落下")
  }

  def nightVision(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.NIGHT_VISION,0,ryoServerAssist,600,"暗視")
  }

  def fireResistance(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.FIRE_RESISTANCE,0,ryoServerAssist,600,"耐火")
  }

  def waterBreathing(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    effect(p,PotionEffectType.WATER_BREATHING,0,ryoServerAssist,600,"水中呼吸")
  }

  def allEffectClear(p:Player): Unit = {
    p.getActivePotionEffects.forEach(effect =>{
      p.removePotionEffect(effect.getType)
    })
    skillMap(p.getName).foreach{case(skillName,_) => {
      skillMap(p.getName)(skillName).cancel()
      val runnableMap = skillMap(p.getName).filterNot{case (name,_) => name == skillName}
      skillMap += (p.getName -> runnableMap)
    }}
    p.sendMessage(ChatColor.AQUA + "スキルをすべて無効化しました。")
  }

}

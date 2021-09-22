package com.ryoserver.SkillSystems.Skill

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.{PotionEffect, PotionEffectType}

class useSkill {

  private def addEffect(p:Player,effectType:PotionEffectType,level:Int): Boolean = {
    if (p.hasPotionEffect(effectType)) {
      p.removePotionEffect(effectType)
      false
    } else {
      p.addPotionEffect(new PotionEffect(effectType,20*60,level))
      true
    }
  }

  val skillOnOffMsg:(String,Boolean,Player) => Unit = (skillName:String,toggle:Boolean,p:Player) => {
    p.sendMessage(ChatColor.AQUA + "スキル:" + skillName + "が" + (if (toggle) "有効化" else "無効化") + "されました。")
  }

  def resistance(p:Player): Unit = {
    skillOnOffMsg("耐性",addEffect(p,PotionEffectType.DAMAGE_RESISTANCE,0),p)
  }

  def speed(p:Player): Unit = {
    skillOnOffMsg("移動速度上昇",addEffect(p,PotionEffectType.SPEED,0),p)
  }

  def jump(p:Player): Unit = {
    skillOnOffMsg("跳躍力上昇",addEffect(p,PotionEffectType.JUMP,0),p)
  }

  def damageUp(p:Player): Unit = {
    skillOnOffMsg("攻撃力上昇",addEffect(p,PotionEffectType.INCREASE_DAMAGE,0),p)
  }

  def diggingUp(p:Player): Unit = {
    skillOnOffMsg("採掘速度上昇",addEffect(p,PotionEffectType.FAST_DIGGING,0),p)
  }

  def regeneration(p:Player): Unit = {
    skillOnOffMsg("再生能力",addEffect(p,PotionEffectType.REGENERATION,0),p)
  }

  def slowFalling(p:Player): Unit = {
    skillOnOffMsg("低速落下",addEffect(p,PotionEffectType.SLOW_FALLING,0),p)
  }

  def nightVision(p:Player): Unit = {
    skillOnOffMsg("暗視",addEffect(p,PotionEffectType.NIGHT_VISION,0),p)
  }

  def fireResistance(p:Player): Unit = {
    skillOnOffMsg("耐火",addEffect(p,PotionEffectType.FIRE_RESISTANCE,0),p)
  }

  def waterBreathing(p:Player): Unit = {
    skillOnOffMsg("水中呼吸",addEffect(p,PotionEffectType.WATER_BREATHING,0),p)
  }

  def allEffectClear(p:Player): Unit = {
    p.getActivePotionEffects.forEach(effect => p.removePotionEffect(effect.getType))
  }

}

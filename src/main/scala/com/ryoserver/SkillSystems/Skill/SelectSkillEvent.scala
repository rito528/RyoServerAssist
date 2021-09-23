package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.potion.PotionEffectType

class SelectSkillEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "スキル選択") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val toggle = new skillToggleClass(p,ryoServerAssist)
    e.getSlot match {
      case 10 => toggle.effect(PotionEffectType.DAMAGE_RESISTANCE,0,300,"耐性")
      case 12 => toggle.effect(PotionEffectType.SPEED,0,300,"移動速度上昇")
      case 14 => toggle.effect(PotionEffectType.JUMP,0,300,"跳躍力上昇")
      case 16 => toggle.effect(PotionEffectType.INCREASE_DAMAGE,0,300,"攻撃力上昇")
      case 28 => toggle.effect(PotionEffectType.FAST_DIGGING,0,300,"採掘速度上昇")
      case 30 => toggle.effect(PotionEffectType.REGENERATION,0,300,"再生能力")
      case 32 => toggle.effect(PotionEffectType.SLOW_FALLING,0,600,"低速落下")
      case 34 => toggle.effect(PotionEffectType.NIGHT_VISION,0,600,"暗視")
      case 46 => toggle.effect(PotionEffectType.FIRE_RESISTANCE,0,600,"火炎耐性")
      case 48 => toggle.effect(PotionEffectType.WATER_BREATHING,0,300,"水中呼吸")
      case 50 => toggle.allEffectClear(p)
      case _ =>
    }
  }

}

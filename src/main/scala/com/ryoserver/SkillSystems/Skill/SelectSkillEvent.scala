package com.ryoserver.SkillSystems.Skill

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class SelectSkillEvent extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "スキル選択") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val skill = new useSkill
    e.getSlot match {
      case 10 => skill.resistance(p)
      case 12 => skill.speed(p)
      case 14 => skill.jump(p)
      case 16 => skill.damageUp(p)
      case 28 => skill.diggingUp(p)
      case 30 => skill.regeneration(p)
      case 32 => skill.slowFalling(p)
      case 34 => skill.nightVision(p)
      case 46 => skill.fireResistance(p)
      case 48 => skill.waterBreathing(p)
      case 50 => skill.allEffectClear(p)
      case _ =>
    }
  }

}

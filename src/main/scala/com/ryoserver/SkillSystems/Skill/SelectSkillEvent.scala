package com.ryoserver.SkillSystems.Skill

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class SelectSkillEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "スキル選択") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    e.getSlot match {
      case 10 => useSkill.resistance(p,ryoServerAssist)
      case 12 => useSkill.speed(p,ryoServerAssist)
      case 14 => useSkill.jump(p,ryoServerAssist)
      case 16 => useSkill.damageUp(p,ryoServerAssist)
      case 28 => useSkill.diggingUp(p,ryoServerAssist)
      case 30 => useSkill.regeneration(p,ryoServerAssist)
      case 32 => useSkill.slowFalling(p,ryoServerAssist)
      case 34 => useSkill.nightVision(p,ryoServerAssist)
      case 46 => useSkill.fireResistance(p,ryoServerAssist)
      case 48 => useSkill.waterBreathing(p,ryoServerAssist)
      case 50 => useSkill.allEffectClear(p)
      case _ =>
    }
  }

}

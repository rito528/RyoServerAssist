package com.ryoserver.SkillSystems.Skill

import com.ryoserver.Menu.createMenu
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SkillData.SkillNames
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
      case 10 => toggle.effect(PotionEffectType.DAMAGE_RESISTANCE,0,300,SkillNames.head)
      case 12 => toggle.effect(PotionEffectType.SPEED,0,300,SkillNames(1))
      case 14 => toggle.effect(PotionEffectType.JUMP,0,300,SkillNames(2))
      case 16 => toggle.effect(PotionEffectType.INCREASE_DAMAGE,0,300,SkillNames(3))
      case 28 => toggle.effect(PotionEffectType.FAST_DIGGING,0,300,SkillNames(4))
      case 30 => toggle.effect(PotionEffectType.REGENERATION,0,300,SkillNames(5))
      case 32 => toggle.effect(PotionEffectType.SLOW_FALLING,0,600,SkillNames(6))
      case 34 => toggle.effect(PotionEffectType.NIGHT_VISION,0,600,SkillNames(7))
      case 46 => toggle.effect(PotionEffectType.FIRE_RESISTANCE,0,600,SkillNames(8))
      case 48 => toggle.effect(PotionEffectType.WATER_BREATHING,0,600,SkillNames(9))
      case 50 => toggle.allEffectClear(p)
      case 52 => p.openInventory(createMenu.menu())
      case _ =>
    }
  }

}

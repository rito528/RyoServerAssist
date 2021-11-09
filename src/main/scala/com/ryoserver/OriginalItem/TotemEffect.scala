package com.ryoserver.OriginalItem

import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.{PotionEffect, PotionEffectType}

class TotemEffect extends Listener {

  @EventHandler
  def onDamage(e: EntityDamageEvent): Unit = {
    if (!e.getEntity.isInstanceOf[Player]) return
    val p = e.getEntity.asInstanceOf[Player]
    val item = p.getInventory.getItemInOffHand
    if (p.getHealth - e.getDamage < 1 && item.getType == OriginalItems.oretaEiyuNoKen.getType &&
      item.getItemMeta.getItemFlags == OriginalItems.oretaEiyuNoKen.getItemMeta.getItemFlags &&
      item.getItemMeta.getDisplayName == OriginalItems.oretaEiyuNoKen.getItemMeta.getDisplayName &&
      item.getItemMeta.getLore == OriginalItems.oretaEiyuNoKen.getItemMeta.getLore) {
      e.setCancelled(true)
      p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 40, 2))
      p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 40, 2))
      p.setHealth(5.0)
      val meta = item.getItemMeta
      if (meta.asInstanceOf[Damageable].getDamage >= 250) {
        p.getInventory.setItemInOffHand(new ItemStack(Material.AIR, 0))
        p.playSound(p.getLocation, Sound.ENTITY_ITEM_BREAK, 1, 1)
      } else {
        meta.asInstanceOf[Damageable].setDamage(meta.asInstanceOf[Damageable].getDamage + 1)
      }
      item.setItemMeta(meta)
      p.playSound(p.getLocation, Sound.ITEM_TOTEM_USE, 1, 1)
    }
  }

}

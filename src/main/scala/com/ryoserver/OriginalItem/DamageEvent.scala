package com.ryoserver.OriginalItem

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.{EventHandler, Listener}

class DamageEvent extends Listener {

  @EventHandler
  def onDamage(e:EntityDamageByEntityEvent): Unit = {
    if (!e.getDamager.isInstanceOf[Player]) return
    val p = e.getDamager.asInstanceOf[Player]
    if (p.getInventory.getItemInOffHand == OriginalItems.tiguruinoyaiba) {
      e.setDamage(e.getDamage() + 3.0)
    }
  }

}

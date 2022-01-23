package com.ryoserver.ExpBottle

import org.bukkit.{Effect, Material, Sound}
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

import java.security.SecureRandom

class UseExpBottle extends Listener {

  @EventHandler
  def interactEvent(e: PlayerInteractEvent): Unit = {
    val p = e.getPlayer
    if (p.isSneaking && (e.getAction == Action.RIGHT_CLICK_BLOCK || e.getAction == Action.RIGHT_CLICK_BLOCK) &&
      p.getInventory.getItemInMainHand.getType == Material.EXPERIENCE_BOTTLE) {
      val expBottleCount = p.getInventory.getItemInMainHand.getAmount
      for (_ <- 0 until expBottleCount) {
        p.getWorld.playEffect(p.getLocation, Effect.POTION_BREAK, 1)
        p.giveExp(3 + new SecureRandom().nextInt(8))
      }
      p.getInventory.setItemInMainHand(null)
    }
  }

}

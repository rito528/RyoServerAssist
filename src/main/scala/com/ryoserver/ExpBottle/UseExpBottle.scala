package com.ryoserver.ExpBottle

import org.bukkit.entity.ExperienceOrb
import org.bukkit.{Effect, Material, Sound}
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

import java.security.SecureRandom

class UseExpBottle extends Listener {

  @EventHandler
  def interactEvent(e: PlayerInteractEvent): Unit = {
    val p = e.getPlayer
    if (p.isSneaking && (e.getAction == Action.RIGHT_CLICK_BLOCK || e.getAction == Action.RIGHT_CLICK_AIR) &&
      p.getInventory.getItemInMainHand.getType == Material.EXPERIENCE_BOTTLE) {
      val expBottleCount = p.getInventory.getItemInMainHand.getAmount
      var exp = 0
      for (_ <- 0 until expBottleCount) {
        p.getWorld.playEffect(p.getLocation, Effect.POTION_BREAK, 1)
        exp += 3 + new SecureRandom().nextInt(8)
      }
      p.getWorld.spawn(p.getLocation,classOf[ExperienceOrb]).setExperience(exp)
      p.getInventory.setItemInMainHand(null)
    }
  }

}

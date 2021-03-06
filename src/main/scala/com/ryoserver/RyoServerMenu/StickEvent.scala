package com.ryoserver.RyoServerMenu

import com.ryoserver.RyoServerAssist
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

class StickEvent(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def stickClick(e: PlayerInteractEvent): Unit = {
    if ((e.getAction == Action.RIGHT_CLICK_BLOCK || e.getAction == Action.RIGHT_CLICK_AIR) &&
      e.getPlayer.getInventory.getItemInMainHand.getType == Material.STICK) {
      new RyoServerMenu1(ryoServerAssist).open(e.getPlayer)
    }
  }

}

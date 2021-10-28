package com.ryoserver.Menu

import com.ryoserver.RyoServerAssist
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

class MenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def stickClick(e: PlayerInteractEvent): Unit = {
    if ((e.getAction == Action.RIGHT_CLICK_BLOCK || e.getAction == Action.RIGHT_CLICK_AIR) &&
      e.getPlayer.getInventory.getItemInMainHand.getType == Material.STICK) {
      new createMenu(ryoServerAssist).menu(e.getPlayer,ryoServerAssist)
    }
  }

}

package com.ryoserver.Elevator

import org.bukkit.event.player.{PlayerMoveEvent, PlayerToggleSneakEvent}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Location, Material}

class ElevatorEvent extends Listener {

  @EventHandler
  def onJump(e: PlayerMoveEvent): Unit = {
    if (e.getPlayer.isFlying || e.getFrom.getY >= e.getTo.getY || e.getFrom.add(0, -1, 0).getBlock.getType != Material.IRON_BLOCK) return
    for (i <- e.getFrom.getY.toInt to 320) {
      val loc = new Location(e.getFrom.getWorld, e.getFrom.getX, i, e.getFrom.getZ)
      if (loc.getBlock.getType == Material.IRON_BLOCK && loc.getY.toInt != e.getFrom.getY.toInt) {
        e.getPlayer.teleport(loc.add(0, 1, 0).setDirection(e.getFrom.getDirection))
        return
      }
    }
  }

  @EventHandler
  def onSneak(e: PlayerToggleSneakEvent): Unit = {
    if (e.getPlayer.getLocation().clone().add(0, -1, 0).getBlock.getType != Material.IRON_BLOCK || !e.getPlayer.isSneaking) return
    for (i <- e.getPlayer.getLocation.getY.toInt to -64 by -1) {
      val loc = new Location(e.getPlayer.getLocation.getWorld, e.getPlayer.getLocation.getX, i, e.getPlayer.getLocation.getZ)
      if (loc.getBlock.getType == Material.IRON_BLOCK && loc.getY != e.getPlayer.getLocation().clone().add(0, -1, 0).getY) {
        e.getPlayer.teleport(loc.add(0, 1, 0).setDirection(e.getPlayer.getLocation.getDirection))
        return
      }
    }
  }

}

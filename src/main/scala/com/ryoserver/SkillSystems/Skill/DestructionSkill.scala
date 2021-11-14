package com.ryoserver.SkillSystems.Skill

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}

class DestructionSkill extends Listener {

  @EventHandler
  def onBreak(e: BlockBreakEvent): Unit = {
    val p = e.getPlayer
    val direction = p.getFacing.toString
    val loc = e.getBlock.getLocation
    if (direction == "NORTH" || direction == "SOUTH") {
      if (loc.getY < p.getLocation.getY) {
        nsBreak(p, loc, -2, -4, 0)
      } else if (loc.getY == p.getLocation.getY) {
        nsBreak(p, loc, -2, 0, 0)
      } else {
        nsBreak(p, loc, -2, -1, 0)
      }
    } else if (direction == "EAST" || direction == "WEST") {
      if (loc.getY < p.getLocation.getY) {
        ewBreak(p, loc, 0, -4, -2)
      } else if (loc.getY == p.getLocation.getY) {
        ewBreak(p, loc, 0, 0, -2)
      } else {
        ewBreak(p, loc, 0, -1, -2)
      }
    }
  }

  /*
    南北で破壊する場合のメソッド
   */
  def nsBreak(p:Player,location: Location,x:Int,y:Int,z:Int): Unit = {
    location.add(x,y,z)
    for (lx <- 0 until 5) {
      for (ly <- 0 until 5) {
        val cloneLocation = location.clone()
        cloneLocation.add(lx, ly, 0)
        cloneLocation.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
      }
    }
  }

  /*
    東西で破壊する場合のメソッド
   */
  def ewBreak(p:Player,location: Location,x:Int,y:Int,z:Int): Unit = {
    location.add(x,y,z)
    for (lz <- 0 until 5) {
      for (ly <- 0 until 5) {
        val cloneLocation = location.clone()
        cloneLocation.add(0, ly, lz)
        cloneLocation.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
      }
    }
  }

}

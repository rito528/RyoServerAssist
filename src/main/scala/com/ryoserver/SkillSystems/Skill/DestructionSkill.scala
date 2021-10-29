package com.ryoserver.SkillSystems.Skill

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack

class DestructionSkill extends Listener {

  @EventHandler
  def onBreak(e: BlockBreakEvent): Unit = {
    val p = e.getPlayer
    val direction = p.getFacing.toString
    if (e.getBlock.getLocation().getY == p.getLocation.getY) return


    if (direction == "NORTH" || direction == "SOUTH") {
      if (e.getBlock.getLocation().getY < p.getLocation.getY) {
        val breakBlockLocation = e.getBlock.getLocation()
        breakBlockLocation.add(-2, -4, 0)
        for (x <- 0 until 5) {
          for (y <- 0 until 5) {
            val cloneLocation = breakBlockLocation.clone()
            cloneLocation.add(x, y, 0)
            if (cloneLocation.getBlock.getType != Material.AIR && cloneLocation.getBlock.getType != Material.CAVE_AIR && cloneLocation.getBlock.getType != Material.VOID_AIR &&
              cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.WATER_CAULDRON  && cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.LAVA) {
              p.getWorld.dropItem(p.getLocation, new ItemStack(cloneLocation.getBlock.getType, 1))
            }
            cloneLocation.getBlock.setType(Material.AIR)
          }
        }
      } else {
        val breakBlockLocation = e.getBlock.getLocation()
        breakBlockLocation.add(-2, -1, 0)
        for (x <- 0 until 5) {
          for (y <- 0 until 5) {
            val cloneLocation = breakBlockLocation.clone()
            cloneLocation.add(x, y, 0)
            if (cloneLocation.getBlock.getType != Material.AIR && cloneLocation.getBlock.getType != Material.CAVE_AIR && cloneLocation.getBlock.getType != Material.VOID_AIR &&
             cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.WATER_CAULDRON  && cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.LAVA) {
              p.getWorld.dropItem(p.getLocation, new ItemStack(cloneLocation.getBlock.getType, 1))
            }
            cloneLocation.getBlock.setType(Material.AIR)
          }
        }
      }
    } else if (direction == "EAST" || direction == "WEST") {
      if (e.getBlock.getLocation().getY < p.getLocation.getY) {
        val breakBlockLocation = e.getBlock.getLocation()
        breakBlockLocation.add(0, -4, -2)
        for (z <- 0 until 5) {
          for (y <- 0 until 5) {
            val cloneLocation = breakBlockLocation.clone()
            cloneLocation.add(0, y, z)
            if (cloneLocation.getBlock.getType != Material.AIR || cloneLocation.getBlock.getType != Material.CAVE_AIR || cloneLocation.getBlock.getType != Material.VOID_AIR)
              p.getWorld.dropItem(p.getLocation, new ItemStack(cloneLocation.getBlock.getType, 1))
            cloneLocation.getBlock.setType(Material.AIR)
          }
        }
      } else {
        val breakBlockLocation = e.getBlock.getLocation()
        breakBlockLocation.add(0, -1, -2)
        for (z <- 0 until 5) {
          for (y <- 0 until 5) {
            val cloneLocation = breakBlockLocation.clone()
            cloneLocation.add(0, y, z)
            if (cloneLocation.getBlock.getType != Material.AIR && cloneLocation.getBlock.getType != Material.CAVE_AIR && cloneLocation.getBlock.getType != Material.VOID_AIR &&
              cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.WATER_CAULDRON  && cloneLocation.getBlock.getType != Material.WATER && cloneLocation.getBlock.getType != Material.LAVA) {
              p.getWorld.dropItem(p.getLocation, new ItemStack(cloneLocation.getBlock.getType, 1))
            }
            cloneLocation.getBlock.setType(Material.AIR)
          }
        }
      }
    }
  }

}

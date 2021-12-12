package com.ryoserver.SkillSystems.Skill

import org.bukkit.{Bukkit, Material}
import org.bukkit.block.BlockFace
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

class FarmSkill extends Listener {

  private val boneMealList = Set(
    Material.WHEAT,
    Material.CARROTS,
    Material.POTATOES,
    Material.BEETROOTS
  )

  @EventHandler
  def interact(e: PlayerInteractEvent): Unit = {
    if (e.getAction != Action.RIGHT_CLICK_BLOCK) return
    if (boneMealList.contains(e.getClickedBlock.getType)) {
      val p = e.getPlayer
      val facing = p.getFacing.toString
      if (facing == "NORTH" || facing == "SOUTH") {
        val minusXLoc = e.getClickedBlock.getLocation().add(-1,0,0)
        for (i <- 0 until 3) {
          val boneMealLoc = minusXLoc.clone().add(i,0,0)
          if (boneMealList.contains(boneMealLoc.getBlock.getType)) {
            boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)
          }
        }
      } else {
        val minusXLoc = e.getClickedBlock.getLocation().add(0,0,-1)
        for (i <- 0 until 3) {
          val boneMealLoc = minusXLoc.clone().add(0,0,i)
          if (boneMealList.contains(boneMealLoc.getBlock.getType)) {
            boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)
          }
        }
      }
    }
  }

}

package com.ryoserver.SkillSystems.Skill.FarmSkill

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.{SkillPointConsumption, SkillPointData}
import com.ryoserver.util.WorldGuardWrapper
import org.bukkit.Material
import org.bukkit.block.{Block, BlockFace}
import org.bukkit.entity.Player

class Grow {

  val notSpecialSkillWorld = List(
    "world",
    "world_nether",
    "world_the_end",
    "trade"
  )
  private val boneMealList = Set(
    Material.WHEAT,
    Material.CARROTS,
    Material.POTATOES,
    Material.BEETROOTS
  )

  def grow(p: Player, skillName: String, clickedBlock: Block, spCost: Int, range: FarmRange): Unit = {
    if (!boneMealList.contains(clickedBlock.getType) || !SpecialSkillPlayerData.isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val facing = p.getFacing.toString
    val worldGuardWrapper = new WorldGuardWrapper
    if (facing == "SOUTH") {
      val minusXLoc = clickedBlock.getLocation().add(-(range.width / 2), 0, 0)
      var cost = 0
      for (x <- 0 until range.width) {
        for (z <- 0 until range.height) {
          val boneMealLoc = minusXLoc.clone().add(x, 0, z)
          if (worldGuardWrapper.isOwner(p, boneMealLoc) || (worldGuardWrapper.isGlobal(boneMealLoc) && !notSpecialSkillWorld.contains(boneMealLoc.getWorld.getName))) {
            if (boneMealList.contains(boneMealLoc.getBlock.getType) && boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)) {
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else if (facing == "NORTH") {
      val minusXLoc = clickedBlock.getLocation().add(-(range.width / 2), 0, 0)
      var cost = 0
      for (x <- 0 until range.width) {
        for (z <- 0 until range.height) {
          val boneMealLoc = minusXLoc.clone().add(x, 0, -z)
          if (worldGuardWrapper.isOwner(p, boneMealLoc) || (worldGuardWrapper.isGlobal(boneMealLoc) && !notSpecialSkillWorld.contains(boneMealLoc.getWorld.getName))) {
            if (boneMealList.contains(boneMealLoc.getBlock.getType) && boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)) {
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else if (facing == "WEST") {
      val minusXLoc = clickedBlock.getLocation().add(0, 0, -(range.width / 2))
      var cost = 0
      for (x <- 0 until range.height) {
        for (z <- 0 until range.width) {
          val boneMealLoc = minusXLoc.clone().add(-x, 0, z)
          if (worldGuardWrapper.isOwner(p, boneMealLoc) || (worldGuardWrapper.isGlobal(boneMealLoc) && !notSpecialSkillWorld.contains(boneMealLoc.getWorld.getName))) {
            if (boneMealList.contains(boneMealLoc.getBlock.getType) && boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)) {
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else {
      val minusXLoc = clickedBlock.getLocation().add(0, 0, -(range.width / 2))
      var cost = 0
      for (x <- 0 until range.height) {
        for (z <- 0 until range.width) {
          val boneMealLoc = minusXLoc.clone().add(x, 0, z)
          if (worldGuardWrapper.isOwner(p, boneMealLoc) || (worldGuardWrapper.isGlobal(boneMealLoc) && !notSpecialSkillWorld.contains(boneMealLoc.getWorld.getName))) {
            if (boneMealList.contains(boneMealLoc.getBlock.getType) && boneMealLoc.getBlock.applyBoneMeal(BlockFace.UP)) {
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    }
  }

}

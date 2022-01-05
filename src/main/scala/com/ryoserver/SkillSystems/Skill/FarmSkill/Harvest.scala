package com.ryoserver.SkillSystems.Skill.FarmSkill

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.{SkillPointConsumption, SkillPointData}
import com.ryoserver.util.WorldGuardWrapper
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

class Harvest {

  val notSpecialSkillWorld = List(
    "world",
    "world_nether",
    "world_the_end",
    "trade"
  )
  private val farmItem = Set(
    Material.WHEAT,
    Material.CARROTS,
    Material.POTATOES,
    Material.BEETROOTS
  )

  def harvest(p: Player, skillName: String, brokeBlock: Block, spCost: Int, range: FarmRange): Unit = {
    if (!farmItem.contains(brokeBlock.getType) || !SpecialSkillPlayerData.isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val facing = p.getFacing.toString
    val worldGuardWrapper = new WorldGuardWrapper
    if (facing == "SOUTH") {
      val minusXLoc = brokeBlock.getLocation().add(-(range.width / 2), 0, 0)
      var cost = 0
      for (x <- 0 until range.width) {
        for (z <- 0 until range.height) {
          val farmItemLoc = minusXLoc.clone().add(x, 0, z)
          if (farmItem.contains(farmItemLoc.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, farmItemLoc) || (worldGuardWrapper.isGlobal(farmItemLoc) && !notSpecialSkillWorld.contains(farmItemLoc.getWorld.getName))) {
              farmItemLoc.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else if (facing == "NORTH") {
      val minusXLoc = brokeBlock.getLocation().add(-(range.width / 2), 0, 0)
      var cost = 0
      for (x <- 0 until range.width) {
        for (z <- 0 until range.height) {
          val farmItemLoc = minusXLoc.clone().add(x, 0, -z)
          if (farmItem.contains(farmItemLoc.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, farmItemLoc) || (worldGuardWrapper.isGlobal(farmItemLoc) && !notSpecialSkillWorld.contains(farmItemLoc.getWorld.getName))) {
              farmItemLoc.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else if (facing == "WEST") {
      val minusXLoc = brokeBlock.getLocation().add(0, 0, -(range.width / 2))
      var cost = 0
      for (x <- 0 until range.height) {
        for (z <- 0 until range.width) {
          val farmItemLoc = minusXLoc.clone().add(-x, 0, z)
          if (farmItem.contains(farmItemLoc.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, farmItemLoc) || (worldGuardWrapper.isGlobal(farmItemLoc) && !notSpecialSkillWorld.contains(farmItemLoc.getWorld.getName))) {
              farmItemLoc.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else {
      val minusXLoc = brokeBlock.getLocation().add(0, 0, -(range.width / 2))
      var cost = 0
      for (x <- 0 until range.height) {
        for (z <- 0 until range.width) {
          val farmItemLoc = minusXLoc.clone().add(x, 0, z)
          if (farmItem.contains(farmItemLoc.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, farmItemLoc) || (worldGuardWrapper.isGlobal(farmItemLoc) && !notSpecialSkillWorld.contains(farmItemLoc.getWorld.getName))) {
              farmItemLoc.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    }
  }


}

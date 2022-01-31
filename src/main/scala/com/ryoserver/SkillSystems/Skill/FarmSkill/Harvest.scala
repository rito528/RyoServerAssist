package com.ryoserver.SkillSystems.Skill.FarmSkill

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.getAutoPlaceSeedsStatus
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.WorldGuardWrapper
import net.coreprotect.CoreProtect
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

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

  private val seeds = Map(
    Material.WHEAT -> Material.WHEAT_SEEDS,
    Material.CARROTS -> Material.CARROT,
    Material.POTATOES -> Material.POTATO,
    Material.BEETROOTS -> Material.BEETROOT_SEEDS
  )

  def harvest(p: Player, skillName: String, brokeBlock: Block, spCost: Int, range: FarmRange): Unit = {
    if (!farmItem.contains(brokeBlock.getType) || !SpecialSkillPlayerData.isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val facing = p.getFacing.toString
    val neoStackGateway = new NeoStackGateway
    val worldGuardWrapper = new WorldGuardWrapper
    val coreProtectAPI = CoreProtect.getInstance().getAPI
    if (facing == "SOUTH") {
      val minusXLoc = brokeBlock.getLocation().add(-(range.width / 2), 0, 0)
      var cost = 0
      for (x <- 0 until range.width) {
        for (z <- 0 until range.height) {
          val farmItemLoc = minusXLoc.clone().add(x, 0, z)
          if (farmItem.contains(farmItemLoc.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, farmItemLoc) || (worldGuardWrapper.isGlobal(farmItemLoc) && !notSpecialSkillWorld.contains(farmItemLoc.getWorld.getName))) {
              coreProtectAPI.logRemoval(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
              farmItemLoc.getBlock.getDrops(p.getInventory.getItemInMainHand).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation, itemStack)
                }
              })
              if (getAutoPlaceSeedsStatus(p.getUniqueId)
                && (p.getInventory.contains(seeds(farmItemLoc.getBlock.getType), 1) ||
                neoStackGateway.getNeoStackAmount(p, new ItemStack(seeds(farmItemLoc.getBlock.getType), 1)) >= 1)) {
                val seed = new ItemStack(farmItemLoc.getBlock.getType, 1)
                if (p.getInventory.contains(farmItemLoc.getBlock.getType, 1)) {
                  p.getInventory.removeItem(seed)
                } else {
                  neoStackGateway.removeNeoStack(p, seed, 1)
                }
                farmItemLoc.getBlock.setType(farmItemLoc.getBlock.getType)
                coreProtectAPI.logPlacement(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
                cost += 1
              } else {
                farmItemLoc.getBlock.setType(Material.AIR)
              }
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
              coreProtectAPI.logRemoval(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
              farmItemLoc.getBlock.getDrops(p.getInventory.getItemInMainHand).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation, itemStack)
                }
              })
              if (getAutoPlaceSeedsStatus(p.getUniqueId)
                && (p.getInventory.contains(seeds(farmItemLoc.getBlock.getType), 1) ||
                neoStackGateway.getNeoStackAmount(p, new ItemStack(seeds(farmItemLoc.getBlock.getType), 1)) >= 1)) {
                val seed = new ItemStack(farmItemLoc.getBlock.getType, 1)
                if (p.getInventory.contains(farmItemLoc.getBlock.getType, 1)) {
                  p.getInventory.removeItem(seed)
                } else {
                  neoStackGateway.removeNeoStack(p, seed, 1)
                }
                farmItemLoc.getBlock.setType(farmItemLoc.getBlock.getType)
                coreProtectAPI.logPlacement(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
                cost += 1
              } else {
                farmItemLoc.getBlock.setType(Material.AIR)
              }
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
              coreProtectAPI.logRemoval(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
              farmItemLoc.getBlock.getDrops(p.getInventory.getItemInMainHand).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation, itemStack)
                }
              })
              if (getAutoPlaceSeedsStatus(p.getUniqueId)
                && (p.getInventory.contains(seeds(farmItemLoc.getBlock.getType), 1) ||
                neoStackGateway.getNeoStackAmount(p, new ItemStack(seeds(farmItemLoc.getBlock.getType), 1)) >= 1)) {
                val seed = new ItemStack(farmItemLoc.getBlock.getType, 1)
                if (p.getInventory.contains(farmItemLoc.getBlock.getType, 1)) {
                  p.getInventory.removeItem(seed)
                } else {
                  neoStackGateway.removeNeoStack(p, seed, 1)
                }
                farmItemLoc.getBlock.setType(farmItemLoc.getBlock.getType)
                coreProtectAPI.logPlacement(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
                cost += 1
              } else {
                farmItemLoc.getBlock.setType(Material.AIR)
              }
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
              coreProtectAPI.logRemoval(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
              farmItemLoc.getBlock.getDrops(p.getInventory.getItemInMainHand).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation, itemStack)
                }
              })
              if (getAutoPlaceSeedsStatus(p.getUniqueId)
                && (p.getInventory.contains(seeds(farmItemLoc.getBlock.getType), 1) ||
                neoStackGateway.getNeoStackAmount(p, new ItemStack(seeds(farmItemLoc.getBlock.getType), 1)) >= 1)) {
                val seed = new ItemStack(farmItemLoc.getBlock.getType, 1)
                if (p.getInventory.contains(farmItemLoc.getBlock.getType, 1)) {
                  p.getInventory.removeItem(seed)
                } else {
                  neoStackGateway.removeNeoStack(p, seed, 1)
                }
                farmItemLoc.getBlock.setType(farmItemLoc.getBlock.getType)
                coreProtectAPI.logPlacement(p.getName, farmItemLoc, farmItemLoc.getBlock.getType, farmItemLoc.getBlock.getBlockData)
                cost += 1
              } else {
                farmItemLoc.getBlock.setType(Material.AIR)
              }
              cost += spCost / (range.width * range.height)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    }
  }


}

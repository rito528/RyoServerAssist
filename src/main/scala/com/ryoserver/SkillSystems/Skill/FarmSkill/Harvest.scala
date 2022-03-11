package com.ryoserver.SkillSystems.Skill.FarmSkill

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.getAutoPlaceSeedsStatus
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.WorldGuardWrapper
import net.coreprotect.CoreProtect
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Location, Material}

import scala.jdk.CollectionConverters.CollectionHasAsScala

final class Harvest {

  //収穫できるアイテム
  private final val farmItem = Set(
    Material.WHEAT,
    Material.CARROTS,
    Material.POTATOES,
    Material.BEETROOTS
  )

  //通常手に入るアイテムと種を紐付けたリスト
  private final val seeds = Map(
    Material.WHEAT -> Material.WHEAT_SEEDS,
    Material.CARROTS -> Material.CARROT,
    Material.POTATOES -> Material.POTATO,
    Material.BEETROOTS -> Material.BEETROOT_SEEDS
  )

  def harvest(p: Player, skillName: String, brokeBlock: Block, spCost: Int, range: FarmRange): Unit = {
    if (!farmItem.contains(brokeBlock.getType) || !SpecialSkillPlayerData.isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val facing = p.getFacing.toString
    val minusXLoc = {
      if (facing == "SOUTH" || facing == "NORTH") brokeBlock.getLocation().add(-(range.width / 2), 0, 0)
      else brokeBlock.getLocation().add(0, 0, -(range.width / 2))
    }
    val location: (Int, Int) => Location = (x: Int, z: Int) => {
      if (facing == "SOUTH") minusXLoc.clone().add(x, 0, z)
      else if (facing == "NORTH") minusXLoc.clone().add(x, 0, -z)
      else if (facing == "WEST") minusXLoc.clone().add(-x, 0, z)
      else minusXLoc.clone().add(x, 0, z)
    }
    val cost = for {
      x <- 0 until range.width
      z <- 0 until range.height
      cost = executeHarvest(p, location(x, z), spCost / (range.width * range.height))
    } yield cost
    new SkillPointConsumption().consumption(cost.sum, p)
  }

  /*
    収穫や植え直しを実行します。
    返り値では追加でかかったスキルポイントを返します。
    skillCostには1ブロック破壊した場合にかかるコストを入れてください。
   */
  private def executeHarvest(p: Player, harvestLocation: Location, skillCost: Double): Double = {
    val block = harvestLocation.getBlock
    val harvestMaterial = block.getType
    if (!farmItem.contains(harvestMaterial)) return 0
    val worldGuardWrapper = new WorldGuardWrapper
    val coreProtectAPI = CoreProtect.getInstance().getAPI
    val neoStackGateway = new NeoStackGateway
    val isOwner = worldGuardWrapper.isOwner(p, harvestLocation)
    val harvestMaterialData = block.getBlockData
    val seed = new ItemStack(seeds(harvestMaterial), 1)
    val uuid = p.getUniqueId
    if (!isOwner && (!worldGuardWrapper.isGlobal(harvestLocation) || getConfig.notSpecialSkillWorlds.contains(harvestLocation.getWorld.getName))) return 0
    coreProtectAPI.logRemoval(p.getName, harvestLocation, harvestMaterial, harvestMaterialData)
    val inventory = p.getInventory
    val hasInventorySeed = inventory.contains(seed.getType)

    //収穫するアイテムのドロップまたは収納
    block.getDrops.asScala.foreach(itemStack => {
      if (p.isAutoStack) {
        neoStackGateway.addStack(itemStack, p)
      } else {
        val playerLocation = p.getLocation
        playerLocation.getWorld.dropItemNaturally(playerLocation, itemStack)
      }
    })

    //ブロックの破壊または植え直し
    if (getAutoPlaceSeedsStatus(uuid) && (hasInventorySeed || neoStackGateway.getNeoStackAmount(p, seed) >= 1)) {
      if (hasInventorySeed) {
        inventory.removeItem(seed)
        p.updateInventory()
      } else {
        neoStackGateway.removeNeoStack(p, seed, 1)
      }
      block.setType(harvestMaterial)
      skillCost + 1
    } else {
      block.setType(Material.AIR)
      skillCost
    }
  }

}

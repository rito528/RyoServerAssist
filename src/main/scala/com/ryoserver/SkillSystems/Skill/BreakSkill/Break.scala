package com.ryoserver.SkillSystems.Skill.BreakSkill

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.NeoStack.NeoStackService
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.isActivatedSkill
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.Item.itemAddDamage
import com.ryoserver.util.WorldGuardWrapper
import net.coreprotect.CoreProtect
import org.bukkit.entity.Player
import org.bukkit.{Location, Material}

import scala.jdk.CollectionConverters.CollectionHasAsScala

final class Break {

  //破壊しないブロック
  val nonBreakBlock = List(
    Material.AIR,
    Material.CAVE_AIR,
    Material.VOID_AIR,
    Material.WATER,
    Material.WATER_CAULDRON,
    Material.LAVA,
    Material.LAVA_CAULDRON,
    Material.BEDROCK
  )

  def break(p: Player, skillName: String, spCost: Int, breakBlockLocation: Location, breakRange: BreakRange): Unit = {
    if (!isActivatedSkill(p, skillName) || spCost > p.getRyoServerData.skillPoint) return
    val facing = p.getFacing.toString
    val playerY = p.getLocation.getY
    val breakBlockY = breakBlockLocation.getY
    /*
      ブロック破壊地点の起点はプレイヤーから見て左下と考える。
      また、横の長さに対して中心を破壊をしたと考える
     */
    val startingLocation: Location = {
      if (facing == "NORTH" || facing == "SOUTH") {
        if (playerY == breakBlockY) breakBlockLocation.add(breakRange.width / 2, 0, 0)
        else if (playerY < breakBlockY) breakBlockLocation.add(breakRange.width / 2, -1, 0)
        else breakBlockLocation.add(breakRange.width / 2, -breakRange.height + 1, 0)
      } else {
        if (playerY == breakBlockY) breakBlockLocation.add(0, 0, breakRange.width / 2)
        else if (playerY < breakBlockY) breakBlockLocation.add(0, -1, breakRange.width / 2)
        else breakBlockLocation.add(0, -breakRange.height + 1, breakRange.width / 2)
      }
    }
    if (facing == "NORTH") startingLocation.add(-breakRange.width / 2 * 2,0,0)
    else if (facing == "EAST") startingLocation.add(0,0,-breakRange.width / 2 * 2)
    val cost = for {
      height <- 0 until breakRange.height
      width <- 0 until breakRange.width
      depth <- 0 until breakRange.depth
      cost = executeBreak(p, getHorizontalDirection(facing, startingLocation.clone(), BreakRange(height, width, depth)),
        spCost / (breakRange.width * breakRange.height * breakRange.depth))
    } yield cost
    new SkillPointConsumption().consumption(cost.sum, p)
  }

  private def executeBreak(p: Player, breakLocation: Location, skillPoint: Double): Double = {
    val block = breakLocation.getBlock
    val breakBlockMaterial = block.getType
    if (nonBreakBlock.contains(breakBlockMaterial)) return 0
    val worldGuardWrapper = new WorldGuardWrapper
    if (!worldGuardWrapper.isOwner(p, breakLocation) &&
      (!worldGuardWrapper.isGlobal(breakLocation) || getConfig.notSpecialSkillWorlds.contains(breakLocation.getWorld.getName)))
      return 0
    val handItem = p.getInventory.getItemInMainHand
    val neoStackService = new NeoStackService
    block.getDrops(handItem).asScala.foreach(itemStack => {
      if (neoStackService.isItemExists(itemStack) && p.getRyoServerData.autoStack) {
         neoStackService.addItemAmount(p.getUniqueId,itemStack,itemStack.getAmount)
      } else {
        p.getLocation.getWorld.dropItem(p.getLocation, itemStack)
      }
    })
    val coreProtectAPI = CoreProtect.getInstance().getAPI
    coreProtectAPI.logRemoval(p.getName, breakLocation, breakBlockMaterial, block.getBlockData)
    block.setType(Material.AIR)
    itemAddDamage(p, handItem)
    skillPoint
  }

  private def getHorizontalDirection(direction: String, startLocation: Location, addRange: BreakRange): Location = {
    if (direction == "NORTH") {
      startLocation.add(addRange.width, addRange.height, -addRange.depth)
    } else if (direction == "SOUTH") {
      startLocation.add(-addRange.width, addRange.height, addRange.depth)
    } else if (direction == "EAST") {
      startLocation.add(addRange.depth, addRange.height, addRange.width)
    } else {
      startLocation.add(addRange.depth, addRange.height, -addRange.width)
    }
  }

}

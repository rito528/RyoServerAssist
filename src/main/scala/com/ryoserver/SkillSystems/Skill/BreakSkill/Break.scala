package com.ryoserver.SkillSystems.Skill.BreakSkill

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.isActivatedSkill
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.Item.itemAddDamage
import com.ryoserver.util.WorldGuardWrapper
import net.coreprotect.CoreProtect
import org.bukkit.entity.Player
import org.bukkit.{Location, Material}

class Break {

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

  val notSpecialSkillWorld = List(
    "world",
    "world_nether",
    "world_the_end",
    "trade"
  )

  def break(p: Player, skillName: String, spCost: Int, breakBlockLocation: Location, breakRange: BreakRange): Unit = {
    if (!isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val direction = p.getFacing.toString
    val worldGuardWrapper = new WorldGuardWrapper
    val handItem = p.getInventory.getItemInMainHand
    val coreProtectAPI = CoreProtect.getInstance().getAPI
    val neoStackGateway = new NeoStackGateway()
    if (direction == "NORTH" || direction == "SOUTH") {
      val breakPoint = {
        if (p.getLocation.getY < breakBlockLocation.getY) breakBlockLocation.add(-(breakRange.x / 2), -(breakRange.y / 2), 0)
        else if (p.getLocation.getY == breakBlockLocation.getY) breakBlockLocation.add(-(breakRange.x / 2), 0, 0)
        else breakBlockLocation.add(-(breakRange.x / 2), -breakRange.y + 1, 0)
      }
      var cost = 0
      for (y <- 0 until breakRange.y) {
        for (x <- 0 until breakRange.x) {
          val pointClone = breakPoint.clone()
          pointClone.add(x, y, 0)
          if (!nonBreakBlock.contains(pointClone.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, pointClone) || (worldGuardWrapper.isGlobal(pointClone) && !notSpecialSkillWorld.contains(pointClone.getWorld.getName))) {
              coreProtectAPI.logRemoval(p.getName,pointClone,pointClone.getBlock.getType,pointClone.getBlock.getBlockData)
              pointClone.getBlock.getDrops(handItem).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation,itemStack)
                }
              })
              pointClone.getBlock.setType(Material.AIR)
              itemAddDamage(p, handItem)
              cost += spCost / (breakRange.x * breakRange.y)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    } else if (direction == "EAST" || direction == "WEST") {
      val breakPoint = {
        if (p.getLocation.getY < breakBlockLocation.getY) breakBlockLocation.add(0, -(breakRange.y / 2), -(breakRange.z / 2))
        else if (p.getLocation.getY == breakBlockLocation.getY) breakBlockLocation.add(0, 0, -(breakRange.z / 2))
        else breakBlockLocation.add(0, -breakRange.y + 1, -(breakRange.z / 2))
      }
      var cost = 0
      for (y <- 0 until breakRange.y) {
        for (z <- 0 until breakRange.z) {
          val pointClone = breakPoint.clone()
          pointClone.add(0, y, z)
          if (!nonBreakBlock.contains(pointClone.getBlock.getType)) {
            if (worldGuardWrapper.isOwner(p, pointClone) || (worldGuardWrapper.isGlobal(pointClone) && !notSpecialSkillWorld.contains(pointClone.getWorld.getName))) {
              coreProtectAPI.logRemoval(p.getName,pointClone,pointClone.getBlock.getType,pointClone.getBlock.getBlockData)
              pointClone.getBlock.getDrops(handItem).forEach(itemStack => {
                if (neoStackGateway.checkItemList(itemStack) && p.isAutoStack) {
                  neoStackGateway.addStack(itemStack, p)
                } else {
                  p.getLocation.getWorld.dropItem(p.getLocation,itemStack)
                }
              })
              pointClone.getBlock.setType(Material.AIR)
              itemAddDamage(p, handItem)
              cost += spCost / (breakRange.z * breakRange.y)
            }
          }
        }
      }
      new SkillPointConsumption().consumption(cost, p)
    }
  }

}

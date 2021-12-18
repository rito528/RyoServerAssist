package com.ryoserver.SkillSystems.Skill.BreakSkill

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.BreakSkill.BreakSkillPlayerData.isActivatedSkill
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.Item.itemAddDamage
import org.bukkit.{Location, Material}
import org.bukkit.entity.Player

class Break {

  val nonBreakBlock = List(
    Material.AIR,
    Material.CAVE_AIR,
    Material.VOID_AIR,
    Material.WATER,
    Material.WATER_CAULDRON,
    Material.LAVA,
    Material.LAVA_CAULDRON
  )

  def break(p: Player, skillName: String, spCost: Int, breakBlockLocation: Location, breakRange: BreakRange): Unit = {
    if (!isActivatedSkill(p, skillName)) return
    val direction = p.getFacing.toString
    val handItem = p.getInventory.getItemInMainHand
    if (direction == "NORTH" || direction == "SOUTH") {
      val breakPoint = {
        if (p.getLocation.getY < breakBlockLocation.getY) breakBlockLocation.add(-(breakRange.x / 2), -(breakRange.y / 2), 0)
        else if (p.getLocation.getY == breakBlockLocation.getY) breakBlockLocation.add(-(breakRange.x / 2), 0, 0)
        else breakBlockLocation.add(-(breakRange.x / 2), -breakRange.y + 1, 0)
      }
      for (y <- 0 until breakRange.y) {
        for (x <- 0 until breakRange.x) {
          val pointClone = breakPoint.clone()
          pointClone.add(x, y, 0)
          if (!nonBreakBlock.contains(pointClone.getBlock)) {
            pointClone.getBlock.breakNaturally(handItem)
            itemAddDamage(p,handItem)
            new SkillPointConsumption().consumption(spCost / (breakRange.x * breakRange.y),p)
          }
        }
      }
    } else if (direction == "EAST" || direction == "WEST") {
      val breakPoint = {
        if (p.getLocation.getY < breakBlockLocation.getY) breakBlockLocation.add(0, -(breakRange.y / 2), -(breakRange.z / 2))
        else if (p.getLocation.getY == breakBlockLocation.getY) breakBlockLocation.add(0, 0, -(breakRange.z / 2))
        else breakBlockLocation.add(0, -breakRange.y + 1, -(breakRange.z / 2))
      }
      for (y <- 0 until breakRange.y) {
        for (z <- 0 until breakRange.z) {
          val pointClone = breakPoint.clone()
          pointClone.add(0, y, z)
          if (!nonBreakBlock.contains(pointClone.getBlock)) {
            pointClone.getBlock.breakNaturally(handItem)
            itemAddDamage(p,handItem)
            new SkillPointConsumption().consumption(spCost / (breakRange.z * breakRange.y),p)
          }
        }
      }
    }
  }

}

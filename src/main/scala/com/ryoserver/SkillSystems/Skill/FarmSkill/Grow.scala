package com.ryoserver.SkillSystems.Skill.FarmSkill

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.SkillPointConsumption
import com.ryoserver.util.WorldGuardWrapper
import org.bukkit.{Location, Material}
import org.bukkit.block.{Block, BlockFace}
import org.bukkit.entity.Player

class Grow {

  private val notSpecialSkillWorld: List[String] = getConfig.notSpecialSkillWorlds

  private val boneMealList = Set(
    Material.WHEAT,
    Material.CARROTS,
    Material.POTATOES,
    Material.BEETROOTS
  )

  /*
    作物を育てる動作を実行します。
    返り値では追加でかかったスキルポイントを返します。
    skillCostには1ブロック破壊した場合にかかるコストを入れてください。
   */
  private def executeGrow(p: Player,growLocation: Location,skillPointCost: Double): Double = {
    val block = growLocation.getBlock
    val growMaterial = block.getType
    if (!boneMealList.contains(growMaterial)) return 0
    val worldGuardWrapper = new WorldGuardWrapper
    val isOwner = worldGuardWrapper.isOwner(p, growLocation)
    if (!isOwner && (!worldGuardWrapper.isGlobal(growLocation) || notSpecialSkillWorld.contains(growLocation.getWorld.getName))) return 0
    if (block.applyBoneMeal(BlockFace.UP)) {
      return skillPointCost
    }
    0
  }

  def grow(p: Player, skillName: String, clickedBlock: Block, spCost: Int, range: FarmRange): Unit = {
    if (!boneMealList.contains(clickedBlock.getType) || !SpecialSkillPlayerData.isActivatedSkill(p, skillName) || spCost > p.getSkillPoint) return
    val facing = p.getFacing.toString
    val minusXLoc = {
      if (facing == "SOUTH" || facing == "NORTH") clickedBlock.getLocation().add(-(range.width / 2), 0, 0)
      else clickedBlock.getLocation().add(0, 0, -(range.width / 2))
    }
    val boneMealLoc: (Int,Int) => Location = (x: Int,z: Int) => {
      if (facing == "SOUTH") minusXLoc.clone().add(x, 0, z)
      else if (facing == "NORTH") minusXLoc.clone().add(x, 0, -z)
      else if (facing == "WEST") minusXLoc.clone().add(-x, 0, z)
      else minusXLoc.clone().add(x, 0, z)
    }
    val cost = for {
      x <- 0 until range.width
      z <- 0 until range.height
      cost = executeGrow(p,boneMealLoc(x,z),spCost / (range.width * range.height))
    } yield cost
    new SkillPointConsumption().consumption(cost.sum, p)
  }

}

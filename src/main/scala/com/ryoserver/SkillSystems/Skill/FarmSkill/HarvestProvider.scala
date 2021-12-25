package com.ryoserver.SkillSystems.Skill.FarmSkill

import enumeratum.{Enum, EnumEntry}
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}

sealed trait HarvestProvider extends Listener with EnumEntry {

  val skillName: String
  val cost: Int
  val range: FarmRange

  @EventHandler
  def breakFarm(e: BlockBreakEvent): Unit = {
    new Harvest().harvest(e.getPlayer, skillName, e.getBlock, cost, range)
  }

}

case class HarvestSkill(override val skillName: String,
                        override val cost: Int,
                        override val range: FarmRange
                       ) extends HarvestProvider

object HarvestSkillAction extends Enum[HarvestProvider] {

  val values: IndexedSeq[HarvestProvider] = findValues

  object wingHarvest extends HarvestSkill("ウイングハーベスト", 9, FarmRange(3, 1))

  object wideHarvest extends HarvestSkill("ワイドハーベスト", 15, FarmRange(5, 1))

  object roundHarvest extends HarvestSkill("ラウンドハーベスト", 27, FarmRange(3, 3))

}

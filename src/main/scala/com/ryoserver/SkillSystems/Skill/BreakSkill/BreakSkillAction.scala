package com.ryoserver.SkillSystems.Skill.BreakSkill

import enumeratum._
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}

sealed trait BreakSkillProvider extends Listener with EnumEntry {

  val skillName: String
  val skillPointCost: Int
  val range: BreakRange

  @EventHandler
  def onBreak(e: BlockBreakEvent): Unit = {
    new Break().break(e.getPlayer, skillName, skillPointCost, e.getBlock.getLocation, range)
  }

}

case class BreakSkill(override val skillName: String,
                      override val skillPointCost: Int,
                      override val range: BreakRange
                     ) extends BreakSkillProvider

object BreakSkillAction extends Enum[BreakSkillProvider] {

  val values: IndexedSeq[BreakSkillProvider] = findValues

  object BreakDuo extends BreakSkill("ブレイク・デュオ", 6, BreakRange(2, 1, 1))

  object UpDownBreak extends BreakSkill("アップダウンブレイク", 9, BreakRange(3, 1, 1))

  object PantingBreak extends BreakSkill("パンチングブレイク", 18, BreakRange(2, 3, 1))

  object TunnelBreak extends BreakSkill("トンネルブレイク", 27, BreakRange(3, 3, 1))

  object wideBreak extends BreakSkill("ワイドブレイク", 45, BreakRange(3, 5, 1))

}

package com.ryoserver.SkillSystems.Skill.FarmSkill

import enumeratum._
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

sealed trait GrowProvider extends Listener with EnumEntry {

  val skillName:String
  val cost: Int
  val range:FarmRange

  @EventHandler
  def interact(e: PlayerInteractEvent): Unit = {
    if (e.getAction != Action.RIGHT_CLICK_BLOCK) return
    new Grow().grow(e.getPlayer, skillName, e.getClickedBlock, cost, range)
  }

}

case class GrowSkill( override val skillName: String,
                      override val cost: Int,
                      override val range:FarmRange
                    ) extends GrowProvider

object GrowSkillAction extends Enum[GrowProvider] {

  val values: IndexedSeq[GrowProvider] = findValues

  object wingGrow extends GrowSkill("ウインググロー",15,FarmRange(3,1))
  object wideGrow extends GrowSkill("ワイドグロー",30,FarmRange(5,1))
  object roundGrow extends GrowSkill("ラウンドグロー",55,FarmRange(3,3))

}


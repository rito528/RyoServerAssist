package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.OriginalItem.OriginalItems
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object OriginalItemCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val p = rawCommandContext.sender.asInstanceOf[Player]
      if (rawCommandContext.args.length < 1) return
      rawCommandContext.args.head match {
        case "勇者の盾" =>
          p.getWorld.dropItem(p.getLocation, OriginalItems.yuusyanotate)
        case "スキル回復(小)" =>
          p.getWorld.dropItemNaturally(p.getLocation, RecoveryItems.min)
        case "スキル回復(中)" =>
          p.getWorld.dropItemNaturally(p.getLocation, RecoveryItems.mid)
        case "スキル回復(大)" =>
          p.getWorld.dropItemNaturally(p.getLocation, RecoveryItems.max)
        case "特等交換券" =>
          p.getWorld.dropItem(p.getLocation, OriginalItems.tokutoukoukanken)
      }
    }

    override val args: List[String] = List("勇者の盾", "スキル回復(小)", "スキル回復(中)", "スキル回復(大)", "特等交換券")

    override val playerCommand: Boolean = true
  })

}

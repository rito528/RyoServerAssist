package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Gacha.SubSystems.GachaItemGetInventory
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object GetGachaItemCommand {

  val executor: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      if (args.length != 1) return
      new GachaItemGetInventory(args.head.toInt).open(sender.asInstanceOf[Player])
    }

    override val args: List[String] = List("0", "1", "2", "3")
  })

}

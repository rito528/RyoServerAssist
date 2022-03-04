package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Home.HomeMenu
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object HomeCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      new HomeMenu().open(rawCommandContext.sender.asInstanceOf[Player])
    }

    override val args: List[String] = Nil

    override val playerCommand: Boolean = true
  })

}

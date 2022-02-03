package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import org.bukkit.Bukkit
import org.bukkit.command.{Command, CommandExecutor, CommandSender, TabExecutor}

object RegionCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      if (rawCommandContext.args.isEmpty) return
      rawCommandContext.args.head.toLowerCase match {
        case "edit" =>
          Bukkit.dispatchCommand(rawCommandContext.sender, "rg bypass")
      }
    }

    override val args: List[String] = List("edit")
  })


}

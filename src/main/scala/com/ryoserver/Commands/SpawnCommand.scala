package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import org.bukkit.Bukkit
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object SpawnCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      rawCommandContext.sender.asInstanceOf[Player].teleport(Bukkit.getWorld("world").getSpawnLocation)
    }

    override val args: List[String] = Nil

    override val playerCommand: Boolean = true
  })

}

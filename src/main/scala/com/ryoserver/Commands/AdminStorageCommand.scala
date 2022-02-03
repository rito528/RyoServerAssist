package com.ryoserver.Commands

import com.ryoserver.AdminStorage.AdminStorage
import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object AdminStorageCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      new AdminStorage().load(rawCommandContext.sender.asInstanceOf[Player])
    }

    override val args: List[String] = Nil
  })
}

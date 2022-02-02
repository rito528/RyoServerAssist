package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.{CommandContext, ContextualTabExecutor, RawCommandContext}
import org.bukkit.command.TabExecutor

object TestCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      sender.sendMessage("testMessage")
      if (args.length == 1 && args.head.toLowerCase() == "a") {
        sender.sendMessage("a!!")
      }
    }

    override val args: List[String] = List("a")
  })

}

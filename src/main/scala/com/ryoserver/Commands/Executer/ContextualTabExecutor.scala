package com.ryoserver.Commands.Executer

import org.bukkit.command.{Command, CommandSender, TabExecutor}

import java.util
import scala.jdk.CollectionConverters._

object ContextualTabExecutor {

  def tabExecuter(commandContext: CommandContext): TabExecutor = new TabExecutor {
    override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
      val context = RawCommandContext(commandSender, ExecutedCommand(command, s), strings.toList)
      commandContext.execute(context)
      true
    }

    override def onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): util.List[String] = {
      if (strings.length != 1) return null
      if (strings(0).isEmpty) {
        return commandContext.args.asJava
      } else {
        commandContext.args.foreach(arg => {
          if (arg.startsWith(strings(0))) {
            return List(arg).asJava
          }
        })
      }
      null
    }
  }

}

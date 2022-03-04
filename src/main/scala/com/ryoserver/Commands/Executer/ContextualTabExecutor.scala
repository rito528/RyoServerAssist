package com.ryoserver.Commands.Executer

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, ExecutedCommand, RawCommandContext}
import org.bukkit.ChatColor._
import org.bukkit.command.{Command, CommandException, CommandSender, TabExecutor}
import org.bukkit.entity.Player

import java.util
import scala.jdk.CollectionConverters._

object ContextualTabExecutor {

  def tabExecuter(commandContext: CommandContext): TabExecutor = new TabExecutor {
    override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
      if (commandContext.playerCommand && !commandSender.isInstanceOf[Player]) {
        commandSender.sendMessage(s"${RED}このコマンドはゲーム内から実行してください。")
        return true
      }
      try {
        val context = RawCommandContext(commandSender, ExecutedCommand(command, s), strings.toList)
        commandContext.execute(context)
      } catch {
        case _: CommandException =>
          commandSender.sendMessage(s"${RED}引数が不正です。")
      }
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

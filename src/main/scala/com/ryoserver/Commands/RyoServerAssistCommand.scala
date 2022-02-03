package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Config.ConfigData
import com.ryoserver.RyoServerAssist
import com.ryoserver.World.Regeneration.Regeneration
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor

class RyoServerAssistCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      if (rawCommandContext.args.length != 1) return
      val sender = rawCommandContext.sender
      rawCommandContext.args.head.toLowerCase match {
        case "reload" =>
          ConfigData.loadConfig(ryoServerAssist)
          sender.sendMessage(s"${AQUA}configのリロードを行いました。")
      }
    }

    override val args: List[String] = List("reload")
  })

}

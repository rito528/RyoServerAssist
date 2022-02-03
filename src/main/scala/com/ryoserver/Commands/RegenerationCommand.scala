package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.RyoServerAssist
import com.ryoserver.World.Regeneration.Regeneration
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor

class RegenerationCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      Bukkit.broadcastMessage(s"${AQUA}ワールドの再生成を行います。")
      Bukkit.broadcastMessage(s"${AQUA}ラグにご注意ください！")
      new Regeneration().regeneration(ryoServerAssist,isForce = true)
    }

    override val args: List[String] = Nil
  })

}

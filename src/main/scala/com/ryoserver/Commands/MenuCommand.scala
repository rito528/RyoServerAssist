package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class MenuCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      new RyoServerMenu1(ryoServerAssist).open(sender.asInstanceOf[Player])
    }

    override val args: List[String] = Nil

    override val playerCommand: Boolean = true
  })


}

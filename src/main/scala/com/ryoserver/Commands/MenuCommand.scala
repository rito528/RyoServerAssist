package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Menu.RyoServerMenu1
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class MenuCommand(implicit ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(menu)

  private def menu(): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(sender.asInstanceOf[Player])
  }


}

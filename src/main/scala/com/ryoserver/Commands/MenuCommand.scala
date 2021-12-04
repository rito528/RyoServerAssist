package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Menu.RyoServerMenu1
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class MenuCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  private def menu(): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(sender.asInstanceOf[Player])
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(menu)


}

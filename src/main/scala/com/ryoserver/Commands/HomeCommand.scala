package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Home.Home
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class HomeCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  private def home(): Unit = {
    new Home(ryoServerAssist).homeInventory(sender.asInstanceOf[Player])
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(home)

}

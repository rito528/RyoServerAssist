package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Home.HomeMenu
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class HomeCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(home)

  private def home(): Unit = {
    new HomeMenu().openHomeMenu(sender.asInstanceOf[Player])
  }

}

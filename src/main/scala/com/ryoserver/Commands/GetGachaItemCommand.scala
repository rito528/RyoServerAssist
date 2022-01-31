package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Gacha.GachaItemGetInventory
import org.bukkit.entity.Player

class GetGachaItemCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "0" -> gachaItemCommand,
      "1" -> gachaItemCommand,
      "2" -> gachaItemCommand,
      "3" -> gachaItemCommand,
    )
  ).playerCommand()

  def gachaItemCommand(): Unit = {
    new GachaItemGetInventory().openGachaItemGetMenu(sender.asInstanceOf[Player], args(0).toInt)
  }
}

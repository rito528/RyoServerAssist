package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import org.bukkit.entity.Player

class HatCommand extends CommandBuilder {

  private def hat(): Unit = {
    sender match {
      case p:Player =>
        val handItem = p.getInventory.getItemInMainHand
        val headItem = p.getInventory.getHelmet
        if (headItem != null) p.getWorld.dropItemNaturally(p.getLocation(), headItem)
        p.getInventory.remove(handItem)
        p.getInventory.setHelmet(handItem)
    }
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(hat)

}

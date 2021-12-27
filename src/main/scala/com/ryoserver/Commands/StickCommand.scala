package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class StickCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(stick)

  private def stick(): Unit = {
    sender.asInstanceOf[Player].getInventory.addItem(new ItemStack(Material.STICK, 1))
    sender.sendMessage(s"${AQUA}木の棒をインベントリに配布しました。")
  }

}

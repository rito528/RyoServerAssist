package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import org.bukkit.{ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class StickCommand extends CommandBuilder {

  private def stick(): Unit = {
    sender.asInstanceOf[Player].getInventory.addItem(new ItemStack(Material.STICK, 1))
    sender.sendMessage(ChatColor.AQUA + "木の棒をインベントリに配布しました。")
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(stick)

}

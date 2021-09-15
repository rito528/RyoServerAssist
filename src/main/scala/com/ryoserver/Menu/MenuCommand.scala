package com.ryoserver.Menu

import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class MenuCommand extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (!sender.isInstanceOf[Player]) {
      sender.sendMessage(ChatColor.RED + "このコマンドはゲーム内からのみ実行できます！")
      return true
    }
    if (label.equalsIgnoreCase("menu")) {
      sender.asInstanceOf[Player].openInventory(createMenu.menu())
      return true
    }
    false
  }

}

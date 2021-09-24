package com.ryoserver.Menu

import com.ryoserver.RyoServerAssist
import org.bukkit.{ChatColor, Material}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MenuCommand(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (!sender.isInstanceOf[Player]) {
      sender.sendMessage(ChatColor.RED + "このコマンドはゲーム内からのみ実行できます！")
      return true
    }
    if (label.equalsIgnoreCase("menu")) {
      sender.asInstanceOf[Player].openInventory(createMenu.menu(sender.asInstanceOf[Player],ryoServerAssist))
      return true
    } else if (label.equalsIgnoreCase("stick")) {
      sender.asInstanceOf[Player].getInventory.addItem(new ItemStack(Material.STICK,1))
      sender.sendMessage(ChatColor.AQUA + "木の棒をインベントリに配布しました。")
      return true
    }
    false
  }

}

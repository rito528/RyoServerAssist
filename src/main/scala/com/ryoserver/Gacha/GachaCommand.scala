package com.ryoserver.Gacha

import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GachaCommand extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("gacha")) {
      if (args(0).equalsIgnoreCase("help")) {
        sender.sendMessage("+-------------------------------------+")
        sender.sendMessage(ChatColor.AQUA + "/gacha give <all/プレイヤー名> <個数>")
        sender.sendMessage("ガチャ券を配布します。all指定ですべてのプレイヤーの配布ボックスにガチャ券を配布します。")
        sender.sendMessage("+-------------------------------------+")
        return true
      } else if (args(0).equalsIgnoreCase("give") && args.length == 3) {
        /*
          配布ボックス実装後に実装
         */
        if (args(1).equalsIgnoreCase("all")) {
          return true
        } else {
          val is = new ItemStack(GachaPaperData.normal)
          is.setAmount(args(2).toInt)
          sender.asInstanceOf[Player].getInventory.addItem(is)
          return true
        }
      }
    }
    false
  }

}

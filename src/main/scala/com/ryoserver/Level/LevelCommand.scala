package com.ryoserver.Level

import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}

class LevelCommand extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("level")) {
      val lv = new CalLv()
      if (args(0).equalsIgnoreCase("getLevel")) {
        sender.sendMessage(args(1) + "に相当するレベル" + lv.getLevel(args(1).toInt))
        return true
      } else if (args(0).equalsIgnoreCase("getExp")) {
        sender.sendMessage("Lv." + args(1) + "->Lv." + (args(1).toInt + 1) + "までに必要な経験値量:" + lv.getExp(args(1).toInt + 1))
        return true
      } else if (args(0).equalsIgnoreCase("getTotalExp")) {
        sender.sendMessage("Lv." + args(1) + "までに必要な経験値総量:" + lv.getSumTotal(args(1).toInt))
        return true
      } else if (args(0).equalsIgnoreCase("help")) {
        sender.sendMessage("+-------------------------------------+")
        sender.sendMessage(ChatColor.AQUA + "/level getLevel <経験値>")
        sender.sendMessage("指定した経験値に相当するレベルを表示します。")
        sender.sendMessage(ChatColor.AQUA + "/level getExp <レベル>")
        sender.sendMessage("指定したレベルにから次のレベルに到達するまでに必要な経験値量を取得します。")
        sender.sendMessage(ChatColor.AQUA + "/level getTotalExp <レベル>")
        sender.sendMessage("指定したレベルに到達するまでに必要な経験値総量を表示します。")
        sender.sendMessage("+-------------------------------------+")
        return true
      }
    }
    false
  }

}

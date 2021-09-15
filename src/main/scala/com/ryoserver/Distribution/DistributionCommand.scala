package com.ryoserver.Distribution

import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}

class DistributionCommand(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("distribution")) {
      if (args(0).equalsIgnoreCase("give") && args.length == 3) {
        new Distribution(ryoServerAssist).addDistribution(args(1),args(2).toInt,sender)
        return true
      } else if (args(0).equalsIgnoreCase("help") && args.length == 1) {
        sender.sendMessage("+-------------------------------------+")
        sender.sendMessage(ChatColor.AQUA + "/distribution give <normal/fromAdmin> <個数>")
        sender.sendMessage("ガチャ券を配布します。")
        sender.sendMessage("normalで通常ガチャ券、fromAdminで不具合のお詫びガチャ券を指定します。")
        sender.sendMessage("+-------------------------------------+")
        return true
      }
    }
    false
  }

}

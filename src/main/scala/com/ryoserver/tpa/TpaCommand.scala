package com.ryoserver.tpa

import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class TpaCommand(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("tpa")) {
      if (args(0).equalsIgnoreCase("send") && args.length == 2) {
        val target = Bukkit.getPlayer(args(1))
        if (!target.isOnline) {
          sender.sendMessage(ChatColor.RED + args(1) + "は現在オフラインのため、tpaを行うことができません！")
          return true
        } else {
          Tpa.sendTpa(sender.asInstanceOf[Player], target, ryoServerAssist)
          return true
        }
      } else if (args(0).equalsIgnoreCase("accept")) {
        Tpa.acceptTpa(sender.asInstanceOf[Player])
        return true
      } else if (args(0).equalsIgnoreCase("cancel")) {
        Tpa.cancelTpa(sender.asInstanceOf[Player])
        return true
      }
    }
    false
  }

}

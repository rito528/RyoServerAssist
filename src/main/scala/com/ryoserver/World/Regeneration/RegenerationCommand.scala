package com.ryoserver.World.Regeneration

import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}

class RegenerationCommand(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(commandSender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("regeneration")) {
      Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドの再生成を行います。")
      Bukkit.broadcastMessage(ChatColor.AQUA + "ラグにご注意ください！")
      new Regeneration(ryoServerAssist).regeneration(true)
      return true
    }
    false
  }

}

package com.ryoserver

import org.bukkit.Bukkit
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class SubCommands extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("spawn")) {
      val p = sender.asInstanceOf[Player]
      p.teleport(Bukkit.getWorld("world").getSpawnLocation)
      return true
    }
    false
  }

}

package com.ryoserver.SimpleRegion

import org.bukkit.Bukkit
import org.bukkit.command.{Command, CommandExecutor, CommandSender}

class RegionCommand extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("sr")) {
      if (args(0).equalsIgnoreCase("edit")) {
        Bukkit.dispatchCommand(sender,"rg bypass")
        return true
      }
    }
    false
  }


}

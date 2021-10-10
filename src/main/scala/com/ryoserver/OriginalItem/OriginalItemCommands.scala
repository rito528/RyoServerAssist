package com.ryoserver.OriginalItem

import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class OriginalItemCommands extends CommandExecutor {

  override def onCommand(commandSender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("getOriginalItem")) {
      val p = commandSender.asInstanceOf[Player]
      if (args(0).equalsIgnoreCase("tiguruinoyaiba")) {
        p.getWorld.dropItem(p.getLocation,OriginalItems.tiguruinoyaiba)
        return true
      } else if (args(0).equalsIgnoreCase("elementalPickaxe")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.elementalPickaxe)
        return true
      }
    }
    false
  }

}

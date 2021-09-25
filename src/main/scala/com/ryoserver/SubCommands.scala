package com.ryoserver

import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class SubCommands extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean =  {
    if (label.equalsIgnoreCase("hat")) {
      val p = sender.asInstanceOf[Player]
      val handItem = p.getInventory.getItemInMainHand
      val headItem = p.getInventory.getHelmet
      if (headItem != null) p.getWorld.dropItemNaturally(p.getLocation(),headItem)
      p.getInventory.remove(handItem)
      p.getInventory.setHelmet(handItem)
      return true
    }
    false
  }

}

package com.ryoserver.SkillSystems

import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class SkillCommands extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (args.length != 2) return false
    if (label.equalsIgnoreCase("skill")) {
      if (args(0).equalsIgnoreCase("getItem")) {
        if (args(1).equalsIgnoreCase("min")) {
          sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation,RecoveryItems.min)
          return true
        } else if (args(1).equalsIgnoreCase("max")) {
          sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation,RecoveryItems.max)
          return true
        }
      }
    }
    false
  }

}

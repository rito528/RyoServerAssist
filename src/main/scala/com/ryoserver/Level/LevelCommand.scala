package com.ryoserver.Level

import org.bukkit.command.{Command, CommandExecutor, CommandSender}

class LevelCommand extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("level")) {
      if (args(0).equalsIgnoreCase("test")) {
        val lv = new CalLv()
        println("Lv." + lv.getLevel(141983))
        return true
      }
    }
    false
  }

}

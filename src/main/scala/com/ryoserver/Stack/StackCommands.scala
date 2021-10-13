package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class StackCommands(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("stack")) {
      val p = sender.asInstanceOf[Player]
      if (args(0).equalsIgnoreCase("editor")) {
        //new StackGUI(ryoServerAssist).openStackEditor(p,0)
        return true
      }
    }
    false
  }

}

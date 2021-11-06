package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}

import java.util.UUID

class TitleCommands(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("title")) {
      if (args(0).equalsIgnoreCase("add")) {
        if (!TitleData.isEnableTitle(ryoServerAssist, args(1))) {
          sender.sendMessage(ChatColor.RED + "指定した称号は存在しません！")
          return true
        }
        val playerTitleData = new PlayerTitleData(ryoServerAssist)
        if (!playerTitleData.openTitle(Bukkit.getPlayer(UUID.fromString(args(2))), args(1))) {
          sender.sendMessage(ChatColor.RED + "指定したプレイヤーは指定した称号をすでに持っています！")
          return true
        }
        sender.sendMessage(ChatColor.AQUA + args(2) + "に称号" + args(1) + "を付与しました。")
        return true
      } else if (args(0).equalsIgnoreCase("remove")) {
        if (!TitleData.isEnableTitle(ryoServerAssist, args(1))) {
          sender.sendMessage(ChatColor.RED + "指定した称号は存在しません！")
          return true
        }
        val playerTitleData = new PlayerTitleData(ryoServerAssist)
        if (!playerTitleData.removeTitle(args(2), args(1))) {
          sender.sendMessage(ChatColor.RED + "指定したプレイヤーは指定した称号を持っていません！")
          return true
        }
        sender.sendMessage(ChatColor.AQUA + args(2) + "から称号" + args(1) + "を剥奪しました。")
        return true
      }
    }
    false
  }

}

package com.ryoserver.Profile

import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class ProfileSettingCommands(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (args.length != 0 && args.length != 2) {
      sender.sendMessage(s"${RED}引数が不正です。")
      return true
    }
    val gateway = new ProfileGateway(ryoServerAssist)
    if (args.length == 0) {
      val profile = gateway.getProfile(sender.asInstanceOf[Player].getUniqueId.toString)
      sender.sendMessage(s"${AQUA}あなたのプロフィール情報")
      sender.sendMessage(s"${AQUA}Twitter: " + profile("Twitter"))
      sender.sendMessage(s"${AQUA}Discord: " + profile("Discord"))
      sender.sendMessage(s"${AQUA}一言メッセージ: " + profile("Word"))
      return true
    } else {
      args(0).toLowerCase() match {
        case "twitter" =>
          gateway.setProfile("Twitter", args(1))
          sender.sendMessage(s"${AQUA}Twitterを「@${args(1)}」に設定しました。")
        case "discord" =>
          gateway.setProfile("Discord", args(1))
          sender.sendMessage(s"${AQUA}Discordを「${args(1)}」に設定しました。")
        case "word" =>
          gateway.setProfile("Word", args(1))
          sender.sendMessage(s"${AQUA}一言メッセージを「${args(1)}」に設定しました。")
        case _ =>
      }
      return true
    }
    false
  }

}

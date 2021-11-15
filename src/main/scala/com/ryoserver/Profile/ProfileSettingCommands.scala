package com.ryoserver.Profile

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class ProfileSettingCommands(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (args.length != 0 && args.length != 2) {
      sender.sendMessage(ChatColor.RED + "引数が不正です。")
      return true
    }
    if (args.length == 0) {
      val profile = getProfile(sender.asInstanceOf[Player].getUniqueId.toString)
      sender.sendMessage(ChatColor.AQUA + "あなたのプロフィール情報")
      sender.sendMessage(ChatColor.AQUA + "Twitter: " + profile("Twitter"))
      sender.sendMessage(ChatColor.AQUA + "Discord: " + profile("Discord"))
      sender.sendMessage(ChatColor.AQUA + "一言メッセージ: " + profile("Word"))
      return true
    } else {
      args(0).toLowerCase() match {
        case "twitter" =>
          setProfile("Twitter", args(1))
          sender.sendMessage(ChatColor.AQUA + "Twitterを「@" + args(1) + "」に設定しました。")
        case "discord" =>
          setProfile("Discord", args(1))
          sender.sendMessage(ChatColor.AQUA + "Discordを「" + args(1) + "」に設定しました。")
        case "word" =>
          setProfile("Word", args(1))
          sender.sendMessage(ChatColor.AQUA + "一言メッセージを「" + args(1) + "」に設定しました。")
        case _ =>
      }
      return true
    }
    false
  }

  def setProfile(profileName:String,contents:String): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET ${profileName}='${contents}';")
    sql.close()
  }

  def getProfile(uuid:String): Map[String,String] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT Twitter,Discord,Word FROM Players WHERE UUID='$uuid';")
    if (rs.next()) {
      val data = Map(
        "Twitter" -> {if (rs.getString("Twitter").isEmpty) "" else  rs.getString("Twitter")},
        "Discord" -> {if (rs.getString("Discord").isEmpty) "" else  rs.getString("Discord")},
        "Word" -> {if (rs.getString("Word").isEmpty) "" else  rs.getString("Word")},
      )
      sql.close()
      data
    } else {
      sql.close()
      null
    }
  }

}

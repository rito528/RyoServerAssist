package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.{PlayerTitleData, TitleData}
import org.bukkit.Bukkit
import org.bukkit.ChatColor._

import java.util.UUID

class TitleCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "add" -> add,
      "remove" -> remove
    )
  )

  private def add(): Unit = {
    if (!TitleData.isEnableTitle(args(1))) {
      sender.sendMessage(s"${RED}指定した称号は存在しません！")
      return
    }
    val playerTitleData = new PlayerTitleData(ryoServerAssist)
    if (!playerTitleData.openTitle(Bukkit.getPlayer(UUID.fromString(args(2))), args(1))) {
      sender.sendMessage(s"${RED}指定したプレイヤーは指定した称号をすでに持っています！")
      return
    }
    sender.sendMessage(s"$AQUA${args(2)}に称号${args(1)}を付与しました。")
  }

  private def remove(): Unit = {
    if (!TitleData.isEnableTitle(args(1))) {
      sender.sendMessage(s"${RED}指定した称号は存在しません！")
      return
    }
    val playerTitleData = new PlayerTitleData(ryoServerAssist)
    if (!playerTitleData.removeTitle(UUID.fromString(args(2)), args(1))) {
      sender.sendMessage(s"${RED}指定したプレイヤーは指定した称号を持っていません！")
      return
    }
    sender.sendMessage(s"$AQUA${args(2)}から称号${args(1)}を剥奪しました。")
  }

}

package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Title.{PlayerTitleData, TitleData}
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor

import java.util.UUID

object TitleCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val args = rawCommandContext.args
      if (args.length != 3) return
      val sender = rawCommandContext.sender
      if (!TitleData.isEnableTitle(args(1))) {
        sender.sendMessage(s"${RED}指定した称号は存在しません！")
        return
      }
      val playerTitleData = new PlayerTitleData()
      args.head.toLowerCase match {
        case "add" =>
          if (!playerTitleData.openTitle(Bukkit.getPlayer(UUID.fromString(args(2))), args(1))) {
            sender.sendMessage(s"${RED}指定したプレイヤーは指定した称号をすでに持っています！")
            return
          }
          sender.sendMessage(s"$AQUA${args(2)}に称号${args(1)}を付与しました。")
        case "remove" =>
          if (!playerTitleData.removeTitle(UUID.fromString(args(2)), args(1))) {
            sender.sendMessage(s"${RED}指定したプレイヤーは指定した称号を持っていません！")
            return
          }
          sender.sendMessage(s"$AQUA${args(2)}から称号${args(1)}を剥奪しました。")
        case _ =>
      }
    }

    override val args: List[String] = List("add", "remove")
  })

}

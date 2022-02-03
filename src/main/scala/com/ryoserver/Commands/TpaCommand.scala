package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.RyoServerAssist
import com.ryoserver.tpa.Tpa
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class TpaCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      if (rawCommandContext.args.isEmpty) return
      val sender = rawCommandContext.sender
      rawCommandContext.args.head.toLowerCase match {
        case "send" =>
          val target = Bukkit.getPlayer(args(1))
          if (!target.isOnline) {
            sender.sendMessage(s"$RED${args(1)}は現在オフラインのため、tpaを行うことができません！")
          } else {
            Tpa.sendTpa(sender.asInstanceOf[Player], target, ryoServerAssist)
          }
        case "accept" =>
          Tpa.acceptTpa(sender.asInstanceOf[Player])
        case "cancel" =>
          Tpa.cancelTpa(sender.asInstanceOf[Player])
        case "autocancel" =>
          if (Tpa.cancelingTpa.contains(sender.getName)) {
            Tpa.cancelingTpa = Tpa.cancelingTpa.filterNot(_ == sender.getName)
            sender.sendMessage(s"${AQUA}autoCancelを無効にしました。")
          } else {
            Tpa.cancelingTpa += sender.getName
            sender.sendMessage(s"${AQUA}autoCancelを有効にしました。")
          }
      }
    }

    override val args: List[String] = List("send", "accept", "cancel", "autoCancel")

    override val playerCommand: Boolean = true
  })

}

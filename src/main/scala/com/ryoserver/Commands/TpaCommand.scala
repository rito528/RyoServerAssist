package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.tpa.Tpa
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.entity.Player

class TpaCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "send" -> send,
      "accept" -> accept,
      "cancel" -> cancel,
      "autoCancel" -> autoCancel
    )
  ).playerCommand()

  private def send(): Unit = {
    val target = Bukkit.getPlayer(args(1))
    if (!target.isOnline) {
      sender.sendMessage(s"$RED${args(1)}は現在オフラインのため、tpaを行うことができません！")
    } else {
      Tpa.sendTpa(sender.asInstanceOf[Player], target, ryoServerAssist)
    }
  }

  private def accept(): Unit = {
    Tpa.acceptTpa(sender.asInstanceOf[Player])
  }

  private def cancel(): Unit = {
    Tpa.cancelTpa(sender.asInstanceOf[Player])
  }

  private def autoCancel(): Unit = {
    if (Tpa.cancelingTpa.contains(sender.getName)) {
      Tpa.cancelingTpa = Tpa.cancelingTpa.filterNot(_ == sender.getName)
      sender.sendMessage(s"${AQUA}autoCancelを無効にしました。")
    } else {
      Tpa.cancelingTpa += sender.getName
      sender.sendMessage(s"${AQUA}autoCancelを有効にしました。")
    }
  }

}

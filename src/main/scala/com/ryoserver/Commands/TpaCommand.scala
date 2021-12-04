package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.tpa.Tpa
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.entity.Player

class TpaCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  def send(): Unit = {
    val target = Bukkit.getPlayer(args(1))
    if (!target.isOnline) {
      sender.sendMessage(ChatColor.RED + args(1) + "は現在オフラインのため、tpaを行うことができません！")
    } else {
      Tpa.sendTpa(sender.asInstanceOf[Player], target, ryoServerAssist)
    }
  }

  def accept(): Unit = {
    Tpa.acceptTpa(sender.asInstanceOf[Player])
  }

  private def cancel(): Unit = {
    Tpa.cancelTpa(sender.asInstanceOf[Player])
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "send" -> send,
      "accept" -> accept,
      "cancel" -> cancel
    )
  ).playerCommand()

}

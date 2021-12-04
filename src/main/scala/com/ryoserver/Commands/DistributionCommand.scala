package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Distribution.Distribution
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor

class DistributionCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  private def give(): Unit = {
    new Distribution(ryoServerAssist).addDistribution(args(1), args(2).toInt, sender)
  }

  private def help(): Unit = {
    sender.sendMessage("+-------------------------------------+")
    sender.sendMessage(ChatColor.AQUA + "/distribution give <normal/fromAdmin> <個数>")
    sender.sendMessage("ガチャ券を配布します。")
    sender.sendMessage("normalで通常ガチャ券、fromAdminで不具合のお詫びガチャ券を指定します。")
    sender.sendMessage("+-------------------------------------+")
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "give" -> give,
      "help" -> help
    )
  )

}

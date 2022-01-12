package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Distribution.Distribution
import org.bukkit.ChatColor._

class DistributionCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "give" -> give,
      "help" -> help
    )
  )

  private def give(): Unit = {
    new Distribution().addDistribution(args(1), args(2).toInt, sender)
  }

  private def help(): Unit = {
    sender.sendMessage("+-------------------------------------+")
    sender.sendMessage(s"${AQUA}/distribution give <normal/fromAdmin> <個数>")
    sender.sendMessage("ガチャ券を配布します。")
    sender.sendMessage("normalで通常ガチャ券、fromAdminで不具合のお詫びガチャ券を指定します。")
    sender.sendMessage("+-------------------------------------+")
  }

}

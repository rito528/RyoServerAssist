package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Distribution.Distribution
import org.bukkit.ChatColor.AQUA
import org.bukkit.command.TabExecutor

object DistributionCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      if (args.length != 1) return
      args.head.toLowerCase match {
        case "give" =>
          new Distribution().addDistribution(args(1), args(2).toInt, sender)
        case "help" =>
          sender.sendMessage("+-------------------------------------+")
          sender.sendMessage(s"${AQUA}/distribution give <normal/fromAdmin> <個数>")
          sender.sendMessage("ガチャ券を配布します。")
          sender.sendMessage("normalで通常ガチャ券、fromAdminで不具合のお詫びガチャ券を指定します。")
          sender.sendMessage("+-------------------------------------+")
      }
    }

    override val args: List[String] = List("give", "help")
  })

}

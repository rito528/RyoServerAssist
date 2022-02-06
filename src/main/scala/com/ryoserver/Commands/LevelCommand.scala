package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Level.CalLv
import com.ryoserver.Level.Player.UpdateLevel
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object LevelCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      if (args.length < 1) return
      args.head.toLowerCase match {
        case "getlevel" =>
          sender.sendMessage(s"${args(1)}に相当するレベル${lv.getLevel(args(1).toInt, limit = false)}")
        case "getexp" =>
          sender.sendMessage(s"Lv.${args(1)} -> Lv.${(args(1).toInt + 1)}までに必要な経験値量:${lv.getExp(args(1).toInt + 1)}")
        case "gettotalexp" =>
          sender.sendMessage(s"Lv.${args(1)}までに必要な経験値総量:${lv.getSumTotal(args(1).toInt)}")
        case "setexp" =>
          new UpdateLevel().updateExp(args(1).toInt, sender.asInstanceOf[Player])
          sender.sendMessage(s"${AQUA}更新しました。")
        case "log" =>
          for (i <- 0 to args(1).toInt) {
            println(s"$i ${new CalLv().getExp(i)} ${new CalLv().getSumTotal(i)}")
          }
        case "help" =>
          sender.sendMessage("+-------------------------------------+")
          sender.sendMessage(s"$AQUA/level getLevel <経験値>")
          sender.sendMessage("指定した経験値に相当するレベルを表示します。")
          sender.sendMessage(s"$AQUA/level getExp <レベル>")
          sender.sendMessage("指定したレベルにから次のレベルに到達するまでに必要な経験値量を取得します。")
          sender.sendMessage(s"$AQUA/level getTotalExp <レベル>")
          sender.sendMessage("指定したレベルに到達するまでに必要な経験値総量を表示します。")
          sender.sendMessage(s"$AQUA/level setExp <経験値>")
          sender.sendMessage("自分を指定した経験値にします。")
          sender.sendMessage(s"$AQUA/level log <レベル>")
          sender.sendMessage("指定したレベルまで、次のレベルまでの経験値量、レベルに到達するまでの経験値量をログに出します。")
          sender.sendMessage("+-------------------------------------+")
        case _ =>
      }
    }

    override val args: List[String] = List("getLevel", "getExp", "getTotalExp", "setExp", "log", "help")

    override val playerCommand: Boolean = true
  })
  private val lv = new CalLv

}

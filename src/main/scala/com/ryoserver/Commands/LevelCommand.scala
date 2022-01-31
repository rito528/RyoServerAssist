package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Level.CalLv
import com.ryoserver.Level.Player.UpdateLevel
import org.bukkit.ChatColor._
import org.bukkit.entity.Player

class LevelCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "getLevel" -> expToLevel,
      "getExp" -> levelToExp,
      "getTotalExp" -> totalLevelExp,
      "setExp" -> setExp,
      "log" -> loggingExp,
      "help" -> help
    )
  ).playerCommand()
  private val lv = new CalLv

  private def expToLevel(): Unit = {
    sender.sendMessage(s"${args(1)}に相当するレベル${lv.getLevel(args(1).toInt, limit = false)}")
  }

  private def levelToExp(): Unit = {
    sender.sendMessage(s"Lv.${args(1)} -> Lv.${(args(1).toInt + 1)}までに必要な経験値量:${lv.getExp(args(1).toInt + 1)}")
  }

  private def totalLevelExp(): Unit = {
    sender.sendMessage(s"Lv.${args(1)}までに必要な経験値総量:${lv.getSumTotal(args(1).toInt)}")
  }

  private def setExp(): Unit = {
    new UpdateLevel().updateExp(args(1).toInt, sender.asInstanceOf[Player])
    sender.sendMessage(s"${AQUA}更新しました。")
  }

  private def loggingExp(): Unit = {
    for (i <- 0 to args(1).toInt) {
      println(s"$i ${new CalLv().getExp(i)} ${new CalLv().getSumTotal(i)}")
    }
  }

  private def help(): Unit = {
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
  }

}

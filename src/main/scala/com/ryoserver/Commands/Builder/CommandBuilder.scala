package com.ryoserver.Commands.Builder

import org.bukkit.ChatColor._
import org.bukkit.command.{Command, CommandExecutor, CommandSender, TabCompleter}
import org.bukkit.entity.Player

import java.util
import scala.jdk.CollectionConverters._

/*
  コマンドを実装するクラスに、このtraitをmixinすることでコマンドの動作やtab補完を実装します。
 */

trait CommandBuilder extends CommandExecutor with TabCompleter {

  val executor: CommandExecutorBuilder
  var args: Array[String] = Array.empty
  var sender: CommandSender = _

  override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
    try {
      args = strings
      sender = commandSender
      if (executor.playerCommandData && !sender.isInstanceOf[Player]) {
        sender.sendMessage(s"${RED}このコマンドはゲーム内から実行してください！")
        return true
      }
      if (args.isEmpty) {
        executor.nonArgumentExecutor()
      } else {
        if (executor.args.contains(strings(0))) executor.args(strings(0))()
        else failedMessage("引数が不正です。")
      }
      true
    } catch {
      case e: Exception =>
        failedMessage("引数が不正です。")
        true
    }
  }

  def failedMessage(message: String): Unit = {
    sender.sendMessage(s"$RED$message")
  }

  override def onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): util.List[String] = {
    if (strings.length != 1) return null
    if (strings(0).isEmpty) {
      return executor.args.keysIterator.toList.asJava
    } else {
      executor.args.keysIterator.toList.foreach(arg => {
        if (arg.startsWith(strings(0))) {
          return List(arg).asJava
        }
      })
    }
    null
  }

  def successMessage(message: String): Unit = {
    sender.sendMessage(s"$AQUA$message")
  }

}

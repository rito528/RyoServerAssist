package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Security.Players
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.entity.Player

class SecurityCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "playerStatus" -> playerStatus,
      "openInventory" -> openInventory,
      "openEnderChest" -> openEnderChest,
      "hide" -> hide,
      "show" -> show,
      "freeze" -> freeze,
      "unfreeze" -> unfreeze,
      "help" -> help
    )
  ).playerCommand()

  private def playerStatus(): Unit = {
    sender.sendMessage(Players.getPlayerStatus(Bukkit.getPlayer(args(1))))
  }

  private def openInventory(): Unit = {
    sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getInventory)
    sender.sendMessage(s"${AQUA}${args(1)}のインベントリを開きました。")
  }

  private def openEnderChest(): Unit = {
    sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getEnderChest)
    sender.sendMessage(s"$AQUA${args(1)}のエンダーチェストを開きました。")
  }

  private def hide(): Unit = {
    val p = sender.asInstanceOf[Player]
    Bukkit.getOnlinePlayers.forEach(sp => sp.hidePlayer(ryoServerAssist, p))
    Players.hideList :+= p
    Bukkit.broadcastMessage(s"$YELLOW${p.getName}がゲームを退出しました")
  }

  private def show(): Unit = {
    val p = sender.asInstanceOf[Player]
    Bukkit.getOnlinePlayers.forEach(sp => sp.showPlayer(ryoServerAssist, p))
    Players.hideList = Players.hideList.filter(hp => hp != p)
    Bukkit.broadcastMessage(s"$YELLOW${p.getName}がゲームに参加しました")
  }

  private def freeze(): Unit = {
    val p = Bukkit.getPlayer(args(1))
    Players.freezePlayer(p)
  }

  private def unfreeze(): Unit = {
    val p = Bukkit.getPlayer(args(1))
    Players.unFreezePlayer(p)
  }

  private def help(): Unit = {
    sender.sendMessage("/security PlayerStatus [プレイヤー名] - 指定したプレイヤーの情報を確認します。")
    sender.sendMessage("/security openInventory [プレイヤー名] - 指定したプレイヤーのインベントリを開きます。")
    sender.sendMessage("/security openEnderChest [プレイヤー名] - 指定したプレイヤーのエンダーチェストを開きます。")
    sender.sendMessage("/security hide - サーバー内全員から自分自身を非表示にします。")
    sender.sendMessage("/security show - サーバー内全員に自分自身を表示します。")
    sender.sendMessage("/security freeze [プレイヤー名] - 指定したプレイヤーの行動を禁止します。")
    sender.sendMessage("/security unfreeze [プレイヤー名] - 指定したプレイヤーの行動の禁止を解除します。")
    sender.sendMessage("/security help - このコマンドリストを表示します。")
  }

}

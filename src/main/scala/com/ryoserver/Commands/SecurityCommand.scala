package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Security.{Config, Players}
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, ChatColor}

class SecurityCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  private def playerStatus(): Unit = {
    sender.sendMessage(Players.getPlayerStatus(Bukkit.getPlayer(args(1))))
  }

  private def openInventory(): Unit = {
    sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getInventory)
    sender.sendMessage(ChatColor.AQUA + args(1) + "のインベントリを開きました。")
  }

  private def openEnderChest(): Unit = {
    sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getEnderChest)
    sender.sendMessage(ChatColor.AQUA + args(1) + "のエンダーチェストを開きました。")
  }

  private def hide(): Unit = {
    val p = sender.asInstanceOf[Player]
    Bukkit.getOnlinePlayers.forEach(sp => sp.hidePlayer(ryoServerAssist, sp))
    Players.hideList :+= p
    Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName + "がゲームを退出しました")
  }

  private def show(): Unit = {
    val p = sender.asInstanceOf[Player]
    Bukkit.getOnlinePlayers.forEach(sp => sp.showPlayer(ryoServerAssist, p))
    Players.hideList = Players.hideList.filter(hp => hp != p)
    Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName + "がゲームに参加しました")
  }

  private def freeze(): Unit = {
    val p = Bukkit.getPlayer(args(1))
    Players.freezePlayer(p)
  }

  private def unfreeze(): Unit = {
    val p = Bukkit.getPlayer(args(1))
    Players.unFreezePlayer(p)
  }

  private def bind(): Unit = {
    if (args(1).equalsIgnoreCase("on")) {
      ryoServerAssist.getConfig.set("ipInfo", true)
      sender.sendMessage(ChatColor.AQUA + "PlayerStatusコマンドでのIP情報表示機能を有効化しました。")
    } else if (args(1).equalsIgnoreCase("off")) {
      ryoServerAssist.getConfig.set("ipInfo", false)
      sender.sendMessage(ChatColor.AQUA + "PlayerStatusコマンドでのIP情報表示機能を無効化しました。")
    }
  }

  private def reload(): Unit = {
    ryoServerAssist.reloadConfig()
    Config.config = ryoServerAssist.getConfig
    sender.sendMessage(ChatColor.AQUA + "configをリロードしました。")
  }

  private def help(): Unit = {
    sender.sendMessage("/security PlayerStatus [プレイヤー名] - 指定したプレイヤーの情報を確認します。")
    sender.sendMessage("/security openInventory [プレイヤー名] - 指定したプレイヤーのインベントリを開きます。")
    sender.sendMessage("/security openEnderChest [プレイヤー名] - 指定したプレイヤーのエンダーチェストを開きます。")
    sender.sendMessage("/security hide - サーバー内全員から自分自身を非表示にします。")
    sender.sendMessage("/security show - サーバー内全員に自分自身を表示します。")
    sender.sendMessage("/security freeze [プレイヤー名] - 指定したプレイヤーの行動を禁止します。")
    sender.sendMessage("/security unfreeze [プレイヤー名] - 指定したプレイヤーの行動の禁止を解除します。")
    sender.sendMessage("/security reload - configをリロードします。")
    sender.sendMessage("/security bind [on/off] - IPに関する情報を表示するかしないかの設定を行います。")
    sender.sendMessage("/security gui - セキュリティープラグインのGUIを開きます。")
    sender.sendMessage("/security help - このコマンドリストを表示します。")
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "playerStatus" -> playerStatus,
      "openInventory" -> openInventory,
      "openEnderChest" -> openEnderChest,
      "hide" -> hide,
      "show" -> show,
      "freeze" -> freeze,
      "unfreeze" -> unfreeze,
      "bind" -> bind,
      "reload" -> reload,
      "help" -> help
    )
  ).playerCommand()

}

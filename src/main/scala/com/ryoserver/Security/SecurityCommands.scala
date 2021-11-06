package com.ryoserver.Security

import com.ryoserver.RyoServerAssist
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, ChatColor}

class SecurityCommands(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("playerStatus") && args.length == 1) {
      sender.sendMessage(players.getPlayerStatus(Bukkit.getPlayer(args(0))))
    } else if (label.equalsIgnoreCase("openInventory") && args.length == 1) {
      sender match {
        case player: Player =>
          player.openInventory(Bukkit.getPlayer(args(0)).getInventory)
          sender.sendMessage(ChatColor.AQUA + args(0) + "のインベントリを開きました。")
        case _ =>
          sender.sendMessage(ChatColor.RED + "ゲーム外からの実行はできません！")
      }
    } else if (label.equalsIgnoreCase("openEnderChest") && args.length == 1) {
      sender match {
        case player: Player =>
          player.openInventory(Bukkit.getPlayer(args(0)).getEnderChest)
          sender.sendMessage(ChatColor.AQUA + args(0) + "のエンダーチェストを開きました。")
        case _ =>
          sender.sendMessage(ChatColor.RED + "ゲーム外からの実行はできません！")
      }
    } else if (label.equalsIgnoreCase("hide") && args.length == 0) {
      sender match {
        case p: Player =>
          Bukkit.getOnlinePlayers.forEach(sp => sp.hidePlayer(ryoServerAssist, sp))
          players.hideList :+= p
          Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName + " left the game")
        case _ =>
          sender.sendMessage(ChatColor.RED + "ゲーム外からの実行はできません！")
      }
    } else if (label.equalsIgnoreCase("show") && args.length == 0) {
      sender match {
        case p: Player =>
          Bukkit.getOnlinePlayers.forEach(sp => sp.showPlayer(ryoServerAssist, p))
          players.hideList = players.hideList.filter(hp => hp != p)
          Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName + " joined the game")
        case _ =>
          sender.sendMessage(ChatColor.RED + "ゲーム外からの実行はできません！")
      }
    } else if (label.equalsIgnoreCase("freeze") && args.length == 1) {
      val p = Bukkit.getPlayer(args(0))
      players.freezePlayer(p)
    } else if (label.equalsIgnoreCase("unfreeze") && args.length == 1) {
      val p = Bukkit.getPlayer(args(0))
      players.unFreezePlayer(p)
    } else if (label.equalsIgnoreCase("security")) {
      args(0) match {
        case "reload" =>
          ryoServerAssist.reloadConfig()
          Config.config = ryoServerAssist.getConfig
          sender.sendMessage(ChatColor.AQUA + "configをリロードしました。")
        case "bind" =>
          if (args(1).equalsIgnoreCase("on")) {
            ryoServerAssist.getConfig.set("ipInfo", true)
            sender.sendMessage(ChatColor.AQUA + "PlayerStatusコマンドでのIP情報表示機能を有効化しました。")
          } else if (args(1).equalsIgnoreCase("off")) {
            ryoServerAssist.getConfig.set("ipInfo", false)
            sender.sendMessage(ChatColor.AQUA + "PlayerStatusコマンドでのIP情報表示機能を無効化しました。")
          }
        case "help" =>
          sender.sendMessage("/PlayerStatus [プレイヤー名] - 指定したプレイヤーの情報を確認します。")
          sender.sendMessage("/openInventory [プレイヤー名] - 指定したプレイヤーのインベントリを開きます。")
          sender.sendMessage("/openEnderChest [プレイヤー名] - 指定したプレイヤーのエンダーチェストを開きます。")
          sender.sendMessage("/hide - サーバー内全員から自分自身を非表示にします。")
          sender.sendMessage("/show - サーバー内全員に自分自身を表示します。")
          sender.sendMessage("/freeze [プレイヤー名] - 指定したプレイヤーの行動を禁止します。")
          sender.sendMessage("/unfreeze [プレイヤー名] - 指定したプレイヤーの行動の禁止を解除します。")
          sender.sendMessage("/security reload - configをリロードします。")
          sender.sendMessage("/security bind [on/off] - IPに関する情報を表示するかしないかの設定を行います。")
          sender.sendMessage("/security gui - セキュリティープラグインのGUIを開きます。")
          sender.sendMessage("/security help - このコマンドリストを表示します。")
      }
    }
    false
  }

}

package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.RyoServerAssist
import com.ryoserver.Security.Players
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class SecurityCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val args = rawCommandContext.args
      if (args.length < 1) return
      val sender = rawCommandContext.sender
      val p = sender.asInstanceOf[Player]
      args.head.toLowerCase match {
        case "playerstatus" =>
          sender.sendMessage(Players.getPlayerStatus(Bukkit.getPlayer(args(1))))
        case "openinventory" =>
          sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getInventory)
          sender.sendMessage(s"${AQUA}${args(1)}のインベントリを開きました。")
        case "openenderchest" =>
          sender.asInstanceOf[Player].openInventory(Bukkit.getPlayer(args(1)).getEnderChest)
          sender.sendMessage(s"$AQUA${args(1)}のエンダーチェストを開きました。")
        case "hide" =>
          Bukkit.getOnlinePlayers.forEach(sp => sp.hidePlayer(ryoServerAssist, p))
          Players.hideList :+= p
          Bukkit.broadcastMessage(s"$YELLOW${p.getName}がゲームを退出しました")
        case "show" =>
          Bukkit.getOnlinePlayers.forEach(sp => sp.showPlayer(ryoServerAssist, p))
          Players.hideList = Players.hideList.filter(hp => hp != p)
          Bukkit.broadcastMessage(s"$YELLOW${p.getName}がゲームに参加しました")
        case "freeze" =>
          Players.freezePlayer(Bukkit.getPlayer(args(1)))
        case "unfreeze" =>
          Players.unFreezePlayer(Bukkit.getPlayer(args(1)))
        case "help" =>
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

    override val args: List[String] = List("PlayerStatus", "openInventory", "openEnderChest", "hide", "show", "freeze", "unfreeze", "help")

    override val playerCommand: Boolean = true
  })

}

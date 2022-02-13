package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Gacha.SubSystems.GachaAddItemInventory
import com.ryoserver.Gacha.{GachaLoader, GachaPaperData}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GachaCommand(implicit ryoServerAssist: RyoServerAssist) {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      if (args.isEmpty) return
      args.head.toLowerCase match {
        case "help" =>
          sender.sendMessage("+-------------------------------------+")
          sender.sendMessage(s"$AQUA/gacha give <プレイヤー名> <個数>")
          sender.sendMessage("ガチャ券を配布します。")
          sender.sendMessage(s"$AQUA/gacha add")
          sender.sendMessage("ガチャアイテムを追加します。")
          sender.sendMessage(s"$AQUA/gacha remove [ID]")
          sender.sendMessage("ガチャアイテムを削除します。")
          sender.sendMessage(s"$AQUA/gacha list [レアリティ番号(0~3)]")
          sender.sendMessage("ガチャアイテムのリストを表示します。")
          sender.sendMessage("+-------------------------------------+")
        case "give" =>
          val is = new ItemStack(GachaPaperData.normal)
          is.setAmount(args(2).toInt)
          sender.asInstanceOf[Player].getInventory.addItem(is)
          sender.sendMessage(s"$AQUA${sender.getName}にガチャ券を${args(2)}枚配布しました。")
        case "add" =>
          new GachaAddItemInventory(ryoServerAssist).open(sender.asInstanceOf[Player])
        case "remove" =>
          GachaLoader.removeGachaItem(args(1).toInt)
          sender.sendMessage(s"ガチャアイテムID:${args(1)}を削除しました。")
        case "list" =>
          GachaLoader.listGachaItem(args(1).toInt, sender.asInstanceOf[Player])
        case _ =>
      }
    }

    override val args: List[String] = List("help", "give", "add", "remove", "list")
  })

}

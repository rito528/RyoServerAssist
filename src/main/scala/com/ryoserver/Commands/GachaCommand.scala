package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Gacha.{GachaAddItemInventory, GachaLoader, GachaPaperData}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GachaCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "help" -> help,
      "give" -> give,
      "add" -> add,
      "list" -> list,
      "remove" -> remove
    )
  ).playerCommand()

  private def help(): Unit = {
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
  }

  private def give(): Unit = {
    val is = new ItemStack(GachaPaperData.normal)
    is.setAmount(args(2).toInt)
    sender.asInstanceOf[Player].getInventory.addItem(is)
    sender.sendMessage(s"$AQUA${sender.getName}にガチャ券を${args(2)}枚配布しました。")
  }

  private def add(): Unit = {
    new GachaAddItemInventory().openAddInventory(sender.asInstanceOf[Player])
  }

  private def remove(): Unit = {
    GachaLoader.removeGachaItem(args(1).toInt, ryoServerAssist)
    sender.sendMessage(s"ガチャアイテムID:${args(1)}を削除しました。")
  }

  private def list(): Unit = {
    GachaLoader.listGachaItem(ryoServerAssist, args(1).toInt, sender.asInstanceOf[Player])
  }

}

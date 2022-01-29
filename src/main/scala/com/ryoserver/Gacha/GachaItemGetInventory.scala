package com.ryoserver.Gacha

import com.ryoserver.Menu.MenuLayout._
import com.ryoserver.Menu.{Menu, MenuItemStack}
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class GachaItemGetInventory extends Menu {

  override val slot: Int = 6
  override var name: String = "ガチャアイテム一覧"
  override var p: Player = _

  def openGachaItemGetMenu(player: Player,rarity: Int): Unit = {
    p = player
    val itemList = rarity match {
      case 0 =>
        GachaLoader.missItemList
      case 1 =>
        GachaLoader.perItemList
      case 2 =>
        GachaLoader.bigPerItemList
      case 3 =>
        GachaLoader.specialItemList
    }
    itemList.zipWithIndex.foreach{case (item,index) =>
      setItemStackButton(MenuItemStack(getX(index),getY(index),item)
      .setLeftClickMotion(getItem(_,index)))
    }
    build(new GachaItemGetInventory().openGachaItemGetMenu(_,0))
    open()
  }

  private def getItem(p: Player,index: Int): Unit = {
    val item = p.getOpenInventory.getTopInventory.getItem(index)
    p.getInventory.addItem(item)
    p.sendMessage(s"${AQUA}${item.getItemMeta.getDisplayName}をインベントリに加えました。")
  }

}

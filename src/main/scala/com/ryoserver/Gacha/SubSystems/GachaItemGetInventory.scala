package com.ryoserver.Gacha.SubSystems

import com.ryoserver.Gacha.GachaLoader
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.{Menu, MenuFrame}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GachaItemGetInventory(rarity: Int) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "ガチャアイテム一覧")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    (rarity match {
      case 0 =>
        GachaLoader.missItemList
      case 1 =>
        GachaLoader.perItemList
      case 2 =>
        GachaLoader.bigPerItemList
      case 3 =>
        GachaLoader.specialItemList
    }).zipWithIndex.map { case (item, index) =>
      index -> getButton(player, item)
    }.toMap
  }

  private def getButton(p: Player, itemStack: ItemStack): Button = {
    Button(itemStack,
      ButtonMotion { _ =>
        p.getInventory.addItem(itemStack)
        p.sendMessage(s"${itemStack.getItemMeta.getDisplayName}をインベントリに加えました。")
      }
    )
  }


}

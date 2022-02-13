package com.ryoserver.Gacha.SubSystems

import com.ryoserver.Gacha.{GachaLoader, Rarity}
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.{Menu, MenuFrame}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GachaItemGetInventory(rarity: Int) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "ガチャアイテム一覧")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    (rarity match {
      case 0 =>
        GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.miss}.keys
      case 1 =>
        GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.per}.keys
      case 2 =>
        GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.bigPer}.keys
      case 3 =>
        GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.special}.keys
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

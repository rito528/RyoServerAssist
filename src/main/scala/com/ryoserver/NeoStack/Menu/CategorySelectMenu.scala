package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton, MenuFrame, MenuOld}
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.NeoStack.PlayerCategory.{getSelectedCategory, setSelectedCategory}
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class CategorySelectMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(5,"neoStackカテゴリ選択")

  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    if (player.getQuestLevel < 20) return false
    true
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeCategorySelectMenu(player, ryoServerAssist)
    import compute._
    Map (
      getLayOut(3,2) -> mainBlocks,
      getLayOut(5,2) -> mainItems,
      getLayOut(7,2) -> gachaItems,
      getLayOut(2,3) -> tools,
      getLayOut(4,3) -> foods,
      getLayOut(6,3) -> redstones,
      getLayOut(8,3) -> plant,
      getLayOut(1,5) -> backMenu,
      getLayOut(5,5) -> autoStack,
      getLayOut(7,5) -> topInventoryStack,
      getLayOut(8,5) -> openSelectStackMenu,
      getLayOut(9,5) -> allStack
    )
  }

}

private case class computeCategorySelectMenu(player: Player,ryoServerAssist: RyoServerAssist) {
  private val lore: List[String] = List(s"${GRAY}クリックで開きます。")
  val mainBlocks: Button = Button(
    ItemStackBuilder
      .getDefault(Material.GRASS_BLOCK)
      .title(s"${GREEN}主要ブロック")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"block")
    }
  )

  val mainItems: Button = Button(
    ItemStackBuilder
      .getDefault(Material.LEATHER)
      .title(s"${GREEN}主要ブロック")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"item")
    }
  )

  val gachaItems: Button = Button(
    ItemStackBuilder
      .getDefault(Material.HONEY_BOTTLE)
      .title(s"${GREEN}ガチャアイテム")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"gachaItem")
    }
  )

  val tools: Button = Button(
    ItemStackBuilder
      .getDefault(Material.DIAMOND_PICKAXE)
      .title(s"${GREEN}ツール系")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"tool")
    }
  )

  val foods: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BREAD)
      .title(s"${GREEN}食料系")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"food")
    }
  )

  val redstones:Button = Button(
    ItemStackBuilder
      .getDefault(Material.REDSTONE)
      .title(s"${GREEN}レッドストーン系")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"redstone")
    }
  )

  val plant: Button = Button(
    ItemStackBuilder
      .getDefault(Material.OAK_SAPLING)
      .title(s"${GREEN}植物系")
      .lore(lore)
      .build(),
    ButtonMotion{_ =>
      openStack(player,"plant")
    }
  )

  val autoStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.HOPPER)
      .title(s"${WHITE}自動収納を${if (player.isAutoStack) "off" else "on"}にします。")
      .lore(List(s"${GRAY}クリックで切り替えます。",
        s"${GRAY}現在の状態:${if (player.isAutoStack) s"$GREEN$BOLD${UNDERLINE}on" else s"$RED$BOLD${UNDERLINE}off"}"))
      .build(),
    ButtonMotion{_ =>
      player.toggleAutoStack()
      new CategorySelectMenu(ryoServerAssist).open(player)
    }
  )

  val topInventoryStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST_MINECART)
      .title(s"${GREEN}一番下のスロット以外のアイテムをneoStackに収納します。")
      .lore(List(s"${GRAY}クリックで収納します。"))
      .build(),
    ButtonMotion{_ =>
      val data = new NeoStackGateway()
      player.getInventory.getContents.zipWithIndex.foreach { case (item, index) =>
        if (item != null && index >= 9 && index <= 35) {
          if (new NeoStackGateway().checkItemList(item)) {
            data.addStack(item, player)
            player.getInventory.clear(index)
          }
        }
      }
      player.sendMessage(s"${AQUA}インベントリ内の上部のアイテムをすべてneoStackに収納しました。")
    }
  )

  val openSelectStackMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST)
      .title(s"${GREEN}アイテムを選んでneoStackに収納します。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion{_ =>
      new SelectStackMenu().openSelectStackMenu(player)
    }
  )

  val allStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST_MINECART)
      .title(s"${GREEN}インベントリ内のアイテムをneoStackに収納します。")
      .lore(List(s"${GRAY}クリックで収納します。"))
      .build(),
    ButtonMotion{_ =>
      val data = new NeoStackGateway()
      player.getInventory.getContents.foreach(item => {
        if (item != null) {
          if (new NeoStackGateway().checkItemList(item)) {
            data.addStack(item, player)
            player.getInventory.removeItem(item)
          }
        }
      })
      player.sendMessage(s"${AQUA}インベントリ内のアイテムをすべてneoStackに収納しました。")
    }
  )

  val backMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}メニューに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      new RyoServerMenu1(ryoServerAssist).open(player)
    }
  )

  private def openStack(p: Player, category: String): Unit = {
    val gui = new StackMenu(1,category,ryoServerAssist)
    gui.open(p)
    setSelectedCategory(p, category)
  }
}

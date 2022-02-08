package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.{MenuOld, MenuButton}
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.NeoStack.PlayerCategory.setSelectedCategory
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class CategorySelectMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  override val slot: Int = 5
  override var name: String = "neoStackカテゴリ選択"
  override var p: Player = _

  def openCategorySelectMenu(player: Player): Unit = {
    p = player
    if (p.getQuestLevel >= 20) {
      val lore: List[String] = List(s"${GRAY}クリックで開きます。")
      setButton(MenuButton(3, 2, Material.GRASS_BLOCK, s"${GREEN}主要ブロック", lore)
        .setLeftClickMotion(openStack(_, "block")))
      setButton(MenuButton(5, 2, Material.LEATHER, s"${GREEN}主要アイテム", lore)
        .setLeftClickMotion(openStack(_, "item")))
      setButton(MenuButton(7, 2, Material.HONEY_BOTTLE, s"${GREEN}ガチャアイテム", lore)
        .setLeftClickMotion(openStack(_, "gachaItem")))
      setButton(MenuButton(2, 3, Material.DIAMOND_PICKAXE, s"${GREEN}ツール系", lore)
        .setLeftClickMotion(openStack(_, "tool")))
      setButton(MenuButton(4, 3, Material.BREAD, s"${GREEN}食料系", lore)
        .setLeftClickMotion(openStack(_, "food")))
      setButton(MenuButton(6, 3, Material.REDSTONE, s"${GREEN}レッドストーン系", lore)
        .setLeftClickMotion(openStack(_, "redstone")))
      setButton(MenuButton(8, 3, Material.OAK_SAPLING, s"${GREEN}植物系", lore)
        .setLeftClickMotion(openStack(_, "plant")))
      setButton(MenuButton(1, 5, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
        .setLeftClickMotion(backMenu))
      setButton(MenuButton(5, 5, Material.HOPPER, s"${WHITE}自動収納を${if (p.isAutoStack) "off" else "on"}にします。",
        List(s"${GRAY}クリックで切り替えます。",
          s"${GRAY}現在の状態:${if (p.isAutoStack) s"$GREEN$BOLD${UNDERLINE}on" else s"$RED$BOLD${UNDERLINE}off"}"))
        .setLeftClickMotion(toggleStack)
        .setReload())
      setButton(MenuButton(7, 5, Material.CHEST_MINECART, s"${GREEN}一番下のスロット以外のアイテムをneoStackに収納します。",
        List(s"${GRAY}クリックで収納します。"))
        .setLeftClickMotion(topInventoryStack))
      setButton(MenuButton(8, 5, Material.CHEST, s"${GREEN}アイテムを選んでneoStackに収納します。", List(s"${GRAY}クリックで収納します。"))
        .setLeftClickMotion(openSelectStackMenu))
      setButton(MenuButton(9, 5, Material.CHEST_MINECART, s"${GREEN}インベントリ内のアイテムをneoStackに収納します。", List(s"${GRAY}クリックで収納します。"))
        .setLeftClickMotion(allStack))
      build(new CategorySelectMenu(ryoServerAssist).openCategorySelectMenu)
      open()
      p.playSound(p.getLocation, Sound.BLOCK_SHULKER_BOX_OPEN, 1, 1)
    } else {
      p.sendMessage(s"${RED}neoStackはLv.20以上になると使うことができます。")
    }
  }

  private def openStack(p: Player, category: String): Unit = {
    val gui = new StackMenu(ryoServerAssist)
    gui.openStack(p, 1, category)
    setSelectedCategory(p, category)
  }

  private def backMenu(p: Player): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(p)
  }

  private def toggleStack(p: Player): Unit = {
    p.toggleAutoStack()
  }

  private def openSelectStackMenu(p: Player): Unit = {
    new SelectStackMenu().openSelectStackMenu(p)
  }

  private def topInventoryStack(p: Player): Unit = {
    val data = new NeoStackGateway()
    p.getInventory.getContents.zipWithIndex.foreach { case (item, index) =>
      if (item != null && index >= 9 && index <= 35) {
        if (new NeoStackGateway().checkItemList(item)) {
          data.addStack(item, p)
          p.getInventory.clear(index)
        }
      }
    }
    p.sendMessage(s"${AQUA}インベントリ内の上部のアイテムをすべてneoStackに収納しました。")
  }

  private def allStack(p: Player): Unit = {
    val data = new NeoStackGateway()
    p.getInventory.getContents.foreach(item => {
      if (item != null) {
        if (new NeoStackGateway().checkItemList(item)) {
          data.addStack(item, p)
          p.getInventory.removeItem(item)
        }
      }
    })
    p.sendMessage(s"${AQUA}インベントリ内のアイテムをすべてneoStackに収納しました。")
  }

}

package com.ryoserver.Menu

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.MenuSessions.session
import com.ryoserver.util.Item.{getEnchantEffectItem, getItem, getPlayerSkull}
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}
import org.bukkit.{Bukkit, Material, OfflinePlayer}

import scala.jdk.CollectionConverters._

trait Menu {

  /*
    各Menuで定義必須
   */
  val slot: Int
  var name: String
  var p: Player

  /*
    一部のスロットだけクリックを無効化する場合はtrueにする
   */
  var partButton = false
  /*
    一部をボタンとする場合、ボタンとして認識するSlotを入れる
   */
  var buttons: Array[Int] = Array.empty

  var inv: Option[Inventory] = None

  /*
    openする前に必ず呼び出す必要がある
  */
  def build(openedInvInstance: Player => Unit): Unit = {
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
    MenuData.openedInv += (p.getUniqueId -> openedInvInstance)
  }

  /*
    最後に呼び出す
   */
  def open(): Unit = {
    p.openInventory(inv.get)
  }

  private def setItem(x: Int, y: Int, item: Material, effect: Boolean, title: String, lore: List[String]): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, if (effect) getEnchantEffectItem(item, title, lore.asJava) else getItem(item, title, lore.asJava))
      case Some(inv) =>
        inv.setItem(index, if (effect) getEnchantEffectItem(item, title, lore.asJava) else getItem(item, title, lore.asJava))
    }
  }

  private def setSkullItem(x: Int, y: Int, p: OfflinePlayer, title: String, lore: List[String]): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, getPlayerSkull(p, title, lore))
      case Some(inv) =>
        inv.setItem(index, getPlayerSkull(p, title, lore))
    }
  }

  private def setMotion(button: Button): Unit = {
    if (button.reload && MenuData.reloadButtons.contains(name)) {
      MenuData.reloadButtons = Map(name -> (MenuData.reloadButtons(name) + getLayOut(button.x,button.y)))
    } else if (button.reload) {
      MenuData.reloadButtons = Map(name -> Set(getLayOut(button.x,button.y)))
    }
  }

  def setButton(menuButton: MenuButton): Unit = {
    setItem(menuButton.x,menuButton.y,menuButton.material,effect = menuButton.effect,menuButton.title,menuButton.lore)
    setMotion(menuButton)
  }

  def setSkull(skull: MenuSkull): Unit = {
    setSkullItem(skull.x,skull.y,skull.offlinePlayer,skull.title,skull.lore)
    setMotion(skull)
  }

  def setItemStackButton(button: MenuItemStack): Unit = {
    val index = MenuLayout.getLayOut(button.x,button.y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, button.itemStack)
      case Some(inv) =>
        inv.setItem(index, button.itemStack)
    }
    setMotion(button)
  }


}

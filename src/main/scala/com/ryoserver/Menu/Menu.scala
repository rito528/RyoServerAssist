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
    最後に呼び出す
   */
  def open(): Unit = {
    p.openInventory(inv.get)
  }

  def setItem(x: Int, y: Int, item: Material, effect: Boolean, title: String, lore: List[String]): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, if (effect) getEnchantEffectItem(item, title, lore.asJava) else getItem(item, title, lore.asJava))
      case Some(inv) =>
        inv.setItem(index, if (effect) getEnchantEffectItem(item, title, lore.asJava) else getItem(item, title, lore.asJava))
    }
  }

  def setSkullItem(x: Int, y: Int, p: OfflinePlayer, title: String, lore: List[String]): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, getPlayerSkull(p, title, lore))
      case Some(inv) =>
        inv.setItem(index, getPlayerSkull(p, title, lore))
    }
  }

  @deprecated("新フレームワークを利用してください")
  def setItemStack(x: Int, y: Int, item: ItemStack): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, item)
      case Some(inv) =>
        inv.setItem(index, item)
    }
  }

  def setButton(menuButton: MenuButton): Unit = {
    setItem(menuButton.x,menuButton.y,menuButton.material,effect = menuButton.effect,menuButton.title,menuButton.lore)
    MenuData.rightClickButtons += (name -> (
      if (MenuData.rightClickButtons.contains(name)) MenuData.rightClickButtons(name).updated(getLayOut(menuButton.x,menuButton.y),menuButton.rightFunc)
     else Map(getLayOut(menuButton.x,menuButton.y) -> menuButton.rightFunc)))
    MenuData.leftClickButtons += (name -> (
      if (MenuData.leftClickButtons.contains(name)) MenuData.leftClickButtons(name).updated(getLayOut(menuButton.x,menuButton.y),menuButton.leftFunc)
      else Map(getLayOut(menuButton.x,menuButton.y) -> menuButton.leftFunc)))
    if (menuButton.reload && MenuData.reloadButtons.contains(name)) {
      MenuData.reloadButtons = Map(name -> (MenuData.reloadButtons(name) + getLayOut(menuButton.x,menuButton.y)))
    } else if (menuButton.reload) {
      MenuData.reloadButtons = Map(name -> Set(getLayOut(menuButton.x,menuButton.y)))
    }
  }

  def setSkull(skull: MenuSkull): Unit = {
    setSkullItem(skull.x,skull.y,skull.offlinePlayer,skull.title,skull.lore)
    MenuData.rightClickButtons += (name -> (
      if (MenuData.rightClickButtons.contains(name)) MenuData.rightClickButtons(name).updated(getLayOut(skull.x,skull.y),skull.rightFunc)
      else Map(getLayOut(skull.x,skull.y) -> skull.rightFunc)))
    MenuData.leftClickButtons += (name -> (
      if (MenuData.leftClickButtons.contains(name)) MenuData.leftClickButtons(name).updated(getLayOut(skull.x,skull.y),skull.leftFunc)
      else Map(getLayOut(skull.x,skull.y) -> skull.leftFunc)))
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
    MenuData.rightClickButtons += (name -> (
      if (MenuData.rightClickButtons.contains(name)) MenuData.rightClickButtons(name).updated(getLayOut(button.x,button.y),button.rightFunc)
      else Map(getLayOut(button.x,button.y) -> button.rightFunc)))
    MenuData.leftClickButtons += (name -> (
      if (MenuData.leftClickButtons.contains(name)) MenuData.leftClickButtons(name).updated(getLayOut(button.x,button.y),button.leftFunc)
      else Map(getLayOut(button.x,button.y) -> button.leftFunc)))
    if (button.reload && MenuData.reloadButtons.contains(name)) {
      MenuData.reloadButtons = Map(name -> (MenuData.reloadButtons(name) + getLayOut(button.x,button.y)))
    } else if (button.reload) {
      MenuData.reloadButtons = Map(name -> Set(getLayOut(button.x,button.y)))
    }
  }

  /*
    openする前に必ず呼び出す必要がある
   */
  def build(openedInvInstance: Player => Unit): Unit = {
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
    MenuData.openedInv += (p.getUniqueId -> openedInvInstance)
  }


}

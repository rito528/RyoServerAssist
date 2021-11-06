package com.ryoserver.Menu

import com.ryoserver.util.Item.{getEnchantEffectItem, getItem, getPlayerSkull}
import com.ryoserver.Menu.MenuSessions.session
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

trait Menu {

  /*
    各Menuで定義必須
   */
  var name: String
  val slot: Int
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

  def setSkullItem(x: Int, y: Int, p: Player, title: String, lore: List[String]): Unit = {
    val index = MenuLayout.getLayOut(x, y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index, getPlayerSkull(p, title, lore))
      case Some(inv) =>
        inv.setItem(index, getPlayerSkull(p, title, lore))
    }
  }

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

  def registerMotion(func: (Player, Int) => Unit): Unit = {
    MenuData.data += (name -> func)
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
  }

}

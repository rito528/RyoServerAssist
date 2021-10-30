package com.ryoserver.Menu

import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import com.ryoserver.Menu.MenuSessions.session
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

trait Menu {

  var name: String
  val slot: Int
  var p: Player

  var partButton = false //一部のスロットだけクリックを無効化する場合はtrueにする
  var buttons:Array[Int] = Array.empty //一部をボタンとする場合、ボタンとして認識するSlotを入れる

  var inv: Option[Inventory] = None

  def open(): Unit = {
    p.openInventory(inv.get)
  }

  def setItem(x:Int,y:Int,item:Material,effect:Boolean,title:String,lore:List[String]): Unit = {
    val index = MenuLayout.getLayOut(x,y)
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(index,if (effect) getGachaItem(item,title,lore.asJava) else getItem(item,title,lore.asJava))
      case Some(inv) =>
        inv.setItem(index,if (effect) getGachaItem(item,title,lore.asJava) else getItem(item,title,lore.asJava))
    }
  }

  def registerMotion(func:(Player,Int) => Unit): Unit = {
    MenuData.data += (name -> func)
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
  }

}

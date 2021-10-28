package com.ryoserver.Menu

import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import com.ryoserver.Menu.MenuSessions.session
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

trait Menu {

  val name: String
  val slot: Int
  var p: Player

  var inv: Option[Inventory] = None

  def open(): Unit = {
    p.openInventory(inv.get)
  }

  def setItem(x:Int,y:Int,item:Material,effect:Boolean,title:String,lore:List[String]): Unit = {
    inv match {
      case None =>
        inv = Option(Bukkit.createInventory(session, MenuLayout.getSlot(slot), name))
        inv.get.setItem(MenuLayout.getLayOut(x,y),if (effect) getGachaItem(item,title,lore.asJava) else getItem(item,title,lore.asJava))
      case Some(inv) =>
        inv.setItem(MenuLayout.getLayOut(x,y),if (effect) getGachaItem(item,title,lore.asJava) else getItem(item,title,lore.asJava))
    }
  }

  def registerMotion(func:(Player,Int) => Unit): Unit = {
    MenuData.data += (name -> func)
  }

}

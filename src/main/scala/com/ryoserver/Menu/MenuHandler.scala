package com.ryoserver.Menu

import com.ryoserver.Menu.session.MenuSession
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryCloseEvent}
import org.bukkit.event.{EventHandler, Listener}

class MenuHandler extends Listener {

  @EventHandler
  def inventoryClickEvent(e: InventoryClickEvent): Unit = {
    val p = e.getWhoClicked match {
      case p: Player => p
      case _ => return
    }
    val holder = e.getWhoClicked.getOpenInventory.getTopInventory.getHolder match {
      case session: MenuSession => session
      case _ =>
        return
    }

    //右クリック、左クリック以外を排除
    if (!e.getClick.isLeftClick && !e.getClick.isRightClick) {
      e.setCancelled(true)
      return
    }
    e.setCancelled(true)
    if (e.getClickedInventory != p.getOpenInventory.getTopInventory) {
      if (holder.isPartButton) e.setCancelled(false)
      return
    }
    holder.runMotion(e.getSlot, e)
  }

  @EventHandler
  def inventoryCloseEvent(e: InventoryCloseEvent): Unit = {
    val p = e.getPlayer match {
      case p: Player => p
      case _ => return
    }

    val inv = e.getPlayer.getOpenInventory.getTopInventory
    val holder = inv.getHolder match {
      case session: MenuSession => session
      case _ =>
        return
    }
    if (holder.returnItem) {
      holder.currentLayout.keySet.foreach(index => inv.clear(index))
      val invContents = inv.getContents
      invContents.foreach(itemStack => {
        if (itemStack != null) p.getWorld.dropItem(p.getLocation, itemStack)
      })
      if (!invContents.forall(_ == null)) p.sendMessage(s"${RED}不要なアイテムを返却しました。")
    }
  }

}

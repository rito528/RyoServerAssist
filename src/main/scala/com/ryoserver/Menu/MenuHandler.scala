package com.ryoserver.Menu

import com.ryoserver.Menu.session.MenuSession
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class MenuHandler extends Listener {

  @EventHandler
  def inventoryClickEvent(e: InventoryClickEvent): Unit = {
    val holder = e.getWhoClicked.getOpenInventory.getTopInventory.getHolder match {
      case session: MenuSession => session
      case _ =>
        return
    }

    //右クリック、左クリック以外を排除
    if (!e.getClick.isLeftClick && !e.getClick.isRightClick) {
      return
    }
    holder.runMotion(e.getSlot,e)
  }

}

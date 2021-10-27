package com.ryoserver.Menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.Inventory

class MenuHandler extends Listener {

  @EventHandler
  def inventoryClick(e:InventoryClickEvent): Unit = {
    val p = e.getWhoClicked match {
      case player: Player => player
      case _ => return
    }
    val clickedInventory = e.getClickedInventory match {
      case inv => if (inv == null) return
      case inv:Inventory => inv
    }
    if (clickedInventory != e.getView.getTopInventory) {
      e.setCancelled(true)
      return
    }

  }

}

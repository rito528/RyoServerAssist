package com.ryoserver.AdminStorage

import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class AdminStorageEvent extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "AdminStorage") return
    new AdminStorage().save(e.getInventory)
  }

}

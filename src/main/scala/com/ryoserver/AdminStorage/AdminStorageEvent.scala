package com.ryoserver.AdminStorage

import com.ryoserver.Storage.Storage
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryCloseEvent

class AdminStorageEvent extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "AdminStorage") return
    new AdminStorage().save(e.getInventory)
  }

}

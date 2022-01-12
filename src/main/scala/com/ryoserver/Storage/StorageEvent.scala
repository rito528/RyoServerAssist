package com.ryoserver.Storage

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class StorageEvent extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "Storage") return
    new Storage().save(e.getInventory, e.getPlayer.asInstanceOf[Player])
  }
}

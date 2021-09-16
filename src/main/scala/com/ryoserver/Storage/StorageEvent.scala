package com.ryoserver.Storage

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryCloseEvent

class StorageEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClose(e:InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "Storage") return
    new Storage(ryoServerAssist).save(e.getInventory,e.getPlayer.asInstanceOf[Player])
  }
}

package com.ryoserver.Player.FirstJoin

import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class FirstJoinSettingEvent extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "初参加アイテム設定画面") return
    new FirstJoinGiveItemRepository().store(e.getInventory)
  }

}

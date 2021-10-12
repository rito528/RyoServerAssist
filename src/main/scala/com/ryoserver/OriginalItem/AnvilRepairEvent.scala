package com.ryoserver.OriginalItem

import com.ryoserver.OriginalItem.OriginalItems.{itemList, metaList}
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.{EventHandler, Listener}

class AnvilRepairEvent extends Listener {

  @EventHandler
  def repair(e:PrepareAnvilEvent): Unit = {
    val item = e.getInventory.getItem(0)
    if (item == null) return
    if (itemList.contains(item) || metaList.contains(item.getItemMeta.getItemFlags)) e.getInventory.setRepairCost(100)
  }

}

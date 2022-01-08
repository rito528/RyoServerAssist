package com.ryoserver.OriginalItem

import com.ryoserver.OriginalItem.OriginalItems.{itemList, metaList}
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryType, PrepareAnvilEvent}
import org.bukkit.event.{Event, EventHandler, Listener}

class RepairEvent extends Listener {

  @EventHandler
  def repair(e: PrepareAnvilEvent): Unit = {
    val item = e.getInventory.getItem(0)
    if (item == null) return
    if (itemList.contains(item) || metaList.contains(item.getItemMeta.getItemFlags)) e.getInventory.setRepairCost(100)
  }

  @EventHandler
  def grindStone(e: InventoryClickEvent): Unit = {
    if (e.getClickedInventory != null && e.getClickedInventory.getType == InventoryType.GRINDSTONE && e.getSlot == 2) {
      val item1 = e.getInventory.getItem(0)
      val item2 = e.getInventory.getItem(1)
      if (item1 == null && item2 == null) return
      if (item1 != null && (itemList.contains(item1) || metaList.contains(item1.getItemMeta.getItemFlags)) ||
        item2 != null && (itemList.contains(item2) || metaList.contains(item2.getItemMeta.getItemFlags))) {
        e.setResult(Event.Result.DENY)
      }
    }
  }

}

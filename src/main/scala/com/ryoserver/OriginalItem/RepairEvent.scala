package com.ryoserver.OriginalItem

import com.ryoserver.Gacha.GachaLoader
import com.ryoserver.OriginalItem.OriginalItems.{itemList, metaList}
import com.ryoserver.util.Item
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryType, PrepareAnvilEvent}
import org.bukkit.event.{Event, EventHandler, Listener}

class RepairEvent extends Listener {

  @EventHandler
  def repair(e: PrepareAnvilEvent): Unit = {
    val item = e.getInventory.getItem(0)
    if (item == null) return
    if (GachaLoader.specialItemList.contains(Item.getNonDamageItem(item).orNull) ||
    GachaLoader.bigPerItemList.contains(Item.getNonDamageItem(item).orNull))
      e.getInventory.setRepairCost(100)
  }

  @EventHandler
  def grindStone(e: InventoryClickEvent): Unit = {
    if (e.getClickedInventory != null && e.getClickedInventory.getType == InventoryType.GRINDSTONE && e.getSlot == 2) {
      val item1 = e.getInventory.getItem(0)
      val item2 = e.getInventory.getItem(1)
      if (item1 == null && item2 == null) return
      if (item1 != null && (GachaLoader.specialItemList.contains(Item.getNonDamageItem(item1).orNull) || GachaLoader.bigPerItemList.contains(Item.getNonDamageItem(item1).orNull)) ||
        item2 != null && (GachaLoader.specialItemList.contains(Item.getNonDamageItem(item2).orNull) || GachaLoader.bigPerItemList.contains(Item.getNonDamageItem(item2).orNull))) {
        e.setResult(Event.Result.DENY)
      }
    }
  }

}

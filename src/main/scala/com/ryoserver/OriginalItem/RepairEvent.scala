package com.ryoserver.OriginalItem

import com.ryoserver.Gacha.{GachaLoader, Rarity}
import com.ryoserver.util.Item
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryType, PrepareAnvilEvent}
import org.bukkit.event.{Event, EventHandler, Listener}

class RepairEvent extends Listener {

  @EventHandler
  def repair(e: PrepareAnvilEvent): Unit = {
    val item = e.getInventory.getItem(0)
    if (item == null) return
    if (GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.special}.toList.contains(Item.getNonDamageItem(item).orNull) ||
      GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.bigPer}.toList.contains(Item.getNonDamageItem(item).orNull))
      e.getInventory.setRepairCost(100)
  }

  @EventHandler
  def grindStone(e: InventoryClickEvent): Unit = {
    if (e.getClickedInventory != null && e.getClickedInventory.getType == InventoryType.GRINDSTONE && e.getSlot == 2) {
      val item1 = e.getInventory.getItem(0)
      val item2 = e.getInventory.getItem(1)
      if (item1 == null && item2 == null) return
      if (item1 != null &&
        (GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.special}.contains(Item.getNonDamageItem(item1).orNull) ||
          GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.bigPer}.contains(Item.getNonDamageItem(item1).orNull)) ||
        item2 != null && (GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.special}.contains(Item.getNonDamageItem(item2).orNull) ||
          GachaLoader.getGachaItemData.filter{case (_,rarity) => rarity == Rarity.bigPer}.contains(Item.getNonDamageItem(item2).orNull))) {
        e.setResult(Event.Result.DENY)
      }
    }
  }

}

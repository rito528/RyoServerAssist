package com.ryoserver.Menu.Contexts

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

trait Slot {
  val itemStack: ItemStack

  def motion(inventoryClickEvent: InventoryClickEvent): Unit = {

  }
}

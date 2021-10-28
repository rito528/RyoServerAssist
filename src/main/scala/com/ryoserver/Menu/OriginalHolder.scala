package com.ryoserver.Menu

import org.bukkit.inventory.{Inventory, InventoryHolder}

class OriginalHolder extends InventoryHolder {
  override def getInventory: Inventory = null
}

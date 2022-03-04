package com.ryoserver.Menu

import org.bukkit.Bukkit.createInventory
import org.bukkit.inventory.{Inventory, InventoryHolder}

case class MenuFrame(row: Int, title: String) {
  require(row <= 6)

  def createMenu(holder: InventoryHolder): Inventory = createInventory(holder, row * 9, title)

}

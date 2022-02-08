package com.ryoserver.Menu.Contexts

import com.ryoserver.Menu.session.MenuSessions
import org.bukkit.Bukkit.createInventory
import org.bukkit.inventory.Inventory

case class MenuFrame(row: Int, title: String) {
  require(row <= 6)

  def createMenu(): Inventory = createInventory(MenuSessions.session,row,title)

}

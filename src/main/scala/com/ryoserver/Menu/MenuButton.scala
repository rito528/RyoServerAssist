package com.ryoserver.Menu

import org.bukkit.inventory.ItemStack
import org.bukkit.{Material, OfflinePlayer}

case class MenuButton(override val x: Int,
                      override val y: Int,
                      override var material: Material,
                      override val title: String,
                      override val lore: List[String]) extends Button {
  override var itemStack: ItemStack = _
  override var offlinePlayer: OfflinePlayer = _
}

case class MenuItemStack(override val x: Int,
                         override val y: Int,
                         override var itemStack: ItemStack,
                         override val title: String,
                         override val lore: List[String]) extends Button {
  override var material: Material = _
  override var offlinePlayer: OfflinePlayer = _
}

case class MenuSkull(override val x: Int,
                     override val y: Int,
                     override var offlinePlayer: OfflinePlayer,
                     override val title: String,
                     override val lore: List[String]) extends Button {
  override var material: Material = _
  override var itemStack: ItemStack = _
}

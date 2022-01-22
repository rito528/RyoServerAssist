package com.ryoserver.Menu

import org.bukkit.inventory.ItemStack
import org.bukkit.{Material, OfflinePlayer}

case class MenuButton(x: Int,
                      y: Int,
                      material: Material,
                      title: String,
                      lore: List[String]) extends Button {
}

case class MenuItemStack(x: Int,
                         y: Int,
                         itemStack: ItemStack) extends Button {
}

case class MenuSkull(x: Int,
                     y: Int,
                     offlinePlayer: OfflinePlayer,
                     title: String,
                     lore: List[String]) extends Button {
}

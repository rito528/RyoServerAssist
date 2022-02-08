package com.ryoserver.Menu

import org.bukkit.inventory.ItemStack
import org.bukkit.{Material, OfflinePlayer}
@deprecated
case class MenuButton(x: Int,
                      y: Int,
                      material: Material,
                      title: String,
                      lore: List[String]) extends Button {
}
@deprecated
case class MenuItemStack(x: Int,
                         y: Int,
                         itemStack: ItemStack) extends Button {
}
@deprecated
case class MenuSkull(x: Int,
                     y: Int,
                     offlinePlayer: OfflinePlayer,
                     title: String,
                     lore: List[String]) extends Button {
}

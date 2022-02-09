package com.ryoserver.Menu.Button

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

case class Button(itemStack: ItemStack,motions: List[InventoryClickEvent => ButtonMotion])

case object Button {
  def apply(itemStack: ItemStack, motions: InventoryClickEvent => ButtonMotion*): Button = {
    Button(itemStack,motions.toList)
  }
}

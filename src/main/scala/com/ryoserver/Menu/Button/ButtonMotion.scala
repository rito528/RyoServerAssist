package com.ryoserver.Menu.Button

import org.bukkit.event.inventory.InventoryClickEvent

trait ButtonMotion {

  val clickMotion: InventoryClickEvent => Unit

  def run(inventoryClickEvent: InventoryClickEvent,e: InventoryClickEvent => Unit): ButtonMotion = {
    e.apply(inventoryClickEvent)
    this
  }
}

object ButtonMotion {
  def apply(clickEvent: InventoryClickEvent => Unit): ButtonMotion = {
    new ButtonMotion {
      override val clickMotion: InventoryClickEvent => Unit = clickEvent
    }
  }
}

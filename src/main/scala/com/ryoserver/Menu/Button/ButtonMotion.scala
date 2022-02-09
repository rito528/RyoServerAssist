package com.ryoserver.Menu.Button

import org.bukkit.event.inventory.InventoryClickEvent

trait ButtonMotion {
  def motion(e: InventoryClickEvent): ButtonMotion = {
    this
  }
}

object ButtonMotion {
  def apply(e: InventoryClickEvent): ButtonMotion = {
    new ButtonMotion {}
  }
}

trait LeftClickMotion {
  def apply(e: InventoryClickEvent): ButtonMotion
}

object LeftClickMotion {
  def apply(event:InventoryClickEvent => Unit): InventoryClickEvent => ButtonMotion = (event: InventoryClickEvent) => ButtonMotion.apply(event)
}

trait RightClickMotion {
  def apply(e: InventoryClickEvent): ButtonMotion
}

object RightClickMotion {
  def apply(event:InventoryClickEvent => Unit): InventoryClickEvent => ButtonMotion = (event: InventoryClickEvent) => ButtonMotion.apply(event)
}

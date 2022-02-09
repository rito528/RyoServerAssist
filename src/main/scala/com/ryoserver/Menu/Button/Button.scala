package com.ryoserver.Menu.Button

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

case class Button(itemStack: ItemStack,motions: List[ButtonMotion]) {

  def runMotion(event: InventoryClickEvent): Unit = {
    motions.foreach(motion => {
      motion.run(event,motion.clickMotion)
    })
  }

}

case object Button {
  def apply(itemStack: ItemStack, motions: ButtonMotion*): Button = {
    Button(itemStack,motions.toList)
  }
}

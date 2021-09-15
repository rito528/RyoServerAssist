package com.ryoserver.Menu

import com.ryoserver.Distribution.Distribution
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent

class MenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    e.getSlot match {
      case 0 => new Distribution(ryoServerAssist).receipt(p)
      case _ =>
    }
  }

}

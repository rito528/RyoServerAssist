package com.ryoserver.Menu

import com.ryoserver.Distribution.Distribution
import com.ryoserver.Quest.QuestSelectInventory
import com.ryoserver.RyoServerAssist
import com.ryoserver.Storage.Storage
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent

class MenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (!e.getView.getTitle.equalsIgnoreCase("りょう鯖メニュー")) return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    e.getSlot match {
      case 0 => new Distribution(ryoServerAssist).receipt(p)
      case 2 => new Storage(ryoServerAssist).load(p)
      case 4 => new QuestSelectInventory(ryoServerAssist).selectInventory(p)
      case _ =>
    }
  }

}

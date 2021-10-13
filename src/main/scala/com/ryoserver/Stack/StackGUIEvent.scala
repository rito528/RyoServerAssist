package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class StackGUIEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (!e.getView.getTitle.contains("stackカテゴリ選択") && e.getClickedInventory != e.getView.getTopInventory) return
    e.setCancelled(true)
    val gui = new StackGUI(ryoServerAssist)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val isLeftClick = e.getClick.isRightClick && p.hasPermission("ryoserverassist.stack")
    e.getSlot match {
      case 11 =>
        gui.openStack(p,1,"block",isLeftClick)
      case 13 =>
        gui.openStack(p,1,"item",isLeftClick)
      case 15 =>
        gui.openStack(p,1,"gachaItem",isLeftClick)
    }
  }

}

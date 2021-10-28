package com.ryoserver.Menu

import com.ryoserver.Menu.MenuSessions.session
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class MenuHandler(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def inventoryClick(e:InventoryClickEvent): Unit = {
    val p = e.getWhoClicked match {
      case player: Player => player
      case _ => return
    }
    val clickedInventory = e.getClickedInventory
    if (e.getWhoClicked.getOpenInventory.getTopInventory.getHolder != session) {
      return
    }
    //menuで上以外のクリックを排除
    if (clickedInventory != e.getView.getTopInventory) {
      e.setCancelled(true)
      return
    }
    e.setCancelled(true)
    if (MenuData.data.contains(e.getView.getTitle)) MenuData.data(e.getView.getTitle)(p,e.getSlot)
  }

}

package com.ryoserver.Menu

import com.ryoserver.Menu.session.MenuSessions.session
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class MenuHandler extends Listener {

  def inventoryClickEvent(e: InventoryClickEvent): Unit = {
    val p = e.getWhoClicked match {
      case player: Player => player
      case _ => return
    }

    //プラグインで作成されたMenu以外を排除
    val clickedInventory = e.getClickedInventory
    if (e.getWhoClicked.getOpenInventory.getTopInventory.getHolder != session) {
      return
    }
    //右クリック、左クリック以外を排除
    if (!e.getClick.isLeftClick && !e.getClick.isRightClick) {
      e.setCancelled(true)
      return
    }

  }

}

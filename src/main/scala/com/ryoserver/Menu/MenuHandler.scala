package com.ryoserver.Menu

import com.ryoserver.Menu.Button.LeftClickMotion
import com.ryoserver.Menu.session.MenuSession
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent

class MenuHandler extends Listener {

  @EventHandler
  def inventoryClickEvent(e: InventoryClickEvent): Unit = {
    val p = e.getWhoClicked match {
      case player: Player => player
      case _ => return
    }

    //プラグインで作成されたMenu以外を排除
    val clickedInventory = e.getClickedInventory

    val holder = e.getWhoClicked.getOpenInventory.getTopInventory.getHolder match {
      case session: MenuSession => session
      case _ =>
        return
    }

    //右クリック、左クリック以外を排除
    if (!e.getClick.isLeftClick && !e.getClick.isRightClick) {
      e.setCancelled(true)
      return
    }

  }

}

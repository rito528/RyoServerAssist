package com.ryoserver.Menu

import com.ryoserver.Menu.MenuData._
import com.ryoserver.Menu.MenuSessions.session
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class MenuHandler extends Listener {

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
    val slot = e.getSlot
    val isPartButton = partButton(e.getView.getTitle)
    val title = e.getView.getTitle
    //menuで上以外のクリックを排除
    if (clickedInventory != e.getView.getTopInventory && !isPartButton) {
      e.setCancelled(true)
      return
    }
    //すべてがボタンとなる、もしくは一部がボタンとなる場合でスロットが一致していた場合はクリックをキャンセル
    if (!isPartButton || Buttons(title).contains(slot)) e.setCancelled(true)
    if (data.contains(title)) data(title)(p,slot)
  }

}

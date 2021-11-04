package com.ryoserver.Menu

import com.ryoserver.Menu.MenuData._
import com.ryoserver.Menu.MenuSessions.session
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.scheduler.BukkitRunnable

class MenuHandler(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def inventoryClick(e:InventoryClickEvent): Unit = {
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
    new BukkitRunnable {
      override def run(): Unit = {
        if (data.contains(title)) data(title)(p,slot)
        else if (dataNeedClick.contains(title)) dataNeedClick(title)(p,slot,e.getClick.isRightClick)
      }
    }.runTask(ryoServerAssist)
  }

}

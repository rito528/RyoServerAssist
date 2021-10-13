package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.Stack.PlayerData.{getSelectedCategory, setSelectedCategory}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class StackGUIEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    val title = e.getView.getTitle
    if (e.getClickedInventory != e.getView.getTopInventory) return
    e.setCancelled(true)
    val gui = new StackGUI(ryoServerAssist)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val isRightClick = e.getClick.isRightClick
    val permission = p.hasPermission("ryoserverassist.stack")
    val isEdit = isRightClick && permission
    val index = e.getSlot
      if (title.equalsIgnoreCase("stackカテゴリ選択")) {
         index match {
          case 11 =>
            gui.openStack(p, 1, "block", isEdit)
            setSelectedCategory(p,"block")
          case 13 =>
            gui.openStack(p, 1, "item", isEdit)
            setSelectedCategory(p,"item")
          case 15 =>
            gui.openStack(p, 1, "gachaItem", isEdit)
             setSelectedCategory(p,"gachaItem")
          case _ =>
        }
      } else if (title.contains("stack")) {
        val nowPage = title.replace("stack:","").replace("[Edit]","").toInt
        index match {
          case 45 =>
            val backPage = nowPage - 1
            if (backPage == 0) gui.openCategorySelectGUI(p)
            else gui.openStack(p,backPage,getSelectedCategory(p),isEdit)
          case 49 =>
            if (title.contains("Edit")) gui.openAddGUI(p)
          case 53 =>
            gui.openStack(p,nowPage + 1,getSelectedCategory(p),isEdit)
          case _ =>
        }
      }
  }

}

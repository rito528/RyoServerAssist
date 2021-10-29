package com.ryoserver.Title

import com.ryoserver.Menu.createMenu
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import org.bukkit.{ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent

class TitleInventoryEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (!e.getView.getTitle.contains("称号一覧:")) return
    e.setCancelled(true)
    val page = e.getView.getTitle.split(":")(1).toInt
    val p = e.getWhoClicked.asInstanceOf[Player]
    val index = e.getSlot
    val inv = e.getInventory
    if (index == 45) {
      new createMenu(ryoServerAssist).menu(p, ryoServerAssist)
    } else if (index == 49) {
      new PlayerTitleData(ryoServerAssist).resetSelectTitle(p.getUniqueId.toString)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号をリセットしました。")
    } else if (index == 53) {
      new TitleInventory(ryoServerAssist).openInv(p,page + 1)
    } else if (0 <= index && 44 >= index && inv.getItem(index).getType == Material.NAME_TAG) {
      /*
       解放済みの称号
       */
      val titleName = ChatColor.RESET + inv.getItem(index).getItemMeta.getDisplayName
      new PlayerTitleData(ryoServerAssist).setSelectTitle(p.getUniqueId.toString,titleName)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号:" + titleName + "を設定しました！")
    }
  }

}

package com.ryoserver.DustBox

import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryCloseEvent}

class DustBoxInventoryEvent extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "ゴミ箱") return
    e.getSlot match {
      case 49 =>
        e.setCancelled(true)
        e.getInventory.clear()
        val p = e.getWhoClicked.asInstanceOf[Player]
        p.playSound(p.getLocation,Sound.ITEM_BUCKET_FILL_LAVA,1,1)
        new DustBoxInventory().openDustBox(e.getWhoClicked.asInstanceOf[Player])
      case _ =>
    }
  }

  @EventHandler
  def onClose(e:InventoryCloseEvent): Unit = {
    var isSend = false
    val inv = e.getInventory
    val p = e.getPlayer
    if (e.getView.getTitle != "ゴミ箱") return
    inv.getContents.foreach(is=> {
      if (is != null) {
        if (is.getItemMeta != inv.getItem(49).getItemMeta) {
          p.getWorld.dropItem(p.getLocation(), is)
          if (!isSend) {
            p.sendMessage(ChatColor.RED + "アイテムを返却しました。")
            isSend = true
          }
        }
      }
    })
  }

}

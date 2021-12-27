package com.ryoserver.Quest

import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.Inventory


class QuestSelectMenuEvent extends Listener {

  @EventHandler
  def closeInventory(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle == "納品") returnItem(e.getInventory, e.getPlayer.asInstanceOf[Player])
  }

  def returnItem(inv: Inventory, p: Player): Unit = {
    var isSend = false
    inv.getContents.foreach(is => {
      if (is != null) {
        if (is.getItemMeta != inv.getItem(45).getItemMeta && is.getItemMeta != inv.getItem(46).getItemMeta && (inv.getItem(47) == null || is.getItemMeta != inv.getItem(47).getItemMeta)
          && is.getItemMeta != inv.getItem(53).getItemMeta) {
          p.getWorld.dropItem(p.getLocation(), is)
          if (!isSend) {
            p.sendMessage(s"${RED}不要なアイテムを返却しました。")
            isSend = true
          }
        }
      }
    })
  }

}

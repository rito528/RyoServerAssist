package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.Inventory


class QuestSelectMenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def closeInventory(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle == "納品") returnItem(e.getInventory, e.getPlayer.asInstanceOf[Player])
  }

  def returnItem(inv: Inventory, p: Player): Unit = {
    var isSend = false
    new QuestProcessInventoryMotions(ryoServerAssist).buttonItemRemove(p, p.getOpenInventory.getTopInventory)
    inv.getContents.foreach(is => {
      if (is != null) {
        p.getWorld.dropItem(p.getLocation(), is)
        if (!isSend) {
          p.sendMessage(s"${RED}不要なアイテムを返却しました。")
          isSend = true
        }
      }
    })
  }

}

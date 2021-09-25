package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.Inventory

class GachaAddItemInventoryEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "ガチャアイテム追加メニュー") return
    val inv = e.getInventory
    val p = e.getWhoClicked.asInstanceOf[Player]
    e.getSlot match {
      case 46 =>
        e.setCancelled(true)
        add(inv,0)
        p.sendMessage(ChatColor.AQUA + "はずれガチャアイテムを追加しました。")
      case 48 =>
        e.setCancelled(true)
        add(inv,1)
        p.sendMessage(ChatColor.AQUA + "あたりガチャアイテムを追加しました。")
      case 50 =>
        e.setCancelled(true)
        add(inv,2)
        p.sendMessage(ChatColor.AQUA + "大当たりガチャアイテムを追加しました。")
      case 52 =>
        e.setCancelled(true)
        add(inv,3)
        p.sendMessage(ChatColor.AQUA + "特等ガチャアイテムを追加しました。")
      case _ =>
    }
  }

  def add(inv:Inventory,rarity: Int): Unit = {
    inv.getContents.foreach(is => {
      if (is != null && is.getItemMeta != inv.getItem(46).getItemMeta && is.getItemMeta != inv.getItem(48).getItemMeta &&
        is.getItemMeta != inv.getItem(50).getItemMeta && is.getItemMeta != inv.getItem(52).getItemMeta) {
        GachaLoader.addGachaItem(ryoServerAssist,is,rarity)
      }
    })
  }

}

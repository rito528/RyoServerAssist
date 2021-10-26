package com.ryoserver.World.SimpleRegion

import com.ryoserver.util.WorldGuardWrapper
import com.sk89q.worldguard.protection.flags.Flags
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}

class RegionSettingMenuEvent extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "保護設定メニュー") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val worldGuard = new WorldGuardWrapper
    val region = worldGuard.getRegion(p.getLocation()).head
    e.getSlot match {
      case 1 =>
        worldGuard.removeRegion(p)
        p.sendMessage(AQUA + "保護:" + region.getId + "を削除しました。")
      case 3 => worldGuard.toggleFlag(region,Flags.USE,p)
      case 5 =>
        worldGuard.toggleFlag(region,Flags.INTERACT,p)
      case 7 =>
        worldGuard.toggleFlag(region,Flags.CHEST_ACCESS,p)
      case 19 => worldGuard.toggleFlag(region,Flags.SLEEP,p)
      case 21 => worldGuard.toggleFlag(region,Flags.PLACE_VEHICLE,p)
      case 23 => worldGuard.toggleFlag(region,Flags.DESTROY_VEHICLE,p)
      case _ =>
    }
    new RegionSettingMenu().openMenu(p)
  }

}

package com.ryoserver.World.SimpleRegion

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet
import com.sk89q.worldguard.protection.flags.{Flags, StateFlag}
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
    val container = WorldGuard.getInstance().getPlatform.getRegionContainer
    val regions = container.get(BukkitAdapter.adapt(p.getWorld))
    val set = regions.getApplicableRegions(new Location(BukkitAdapter.adapt(p.getWorld),p.getLocation().getX,p.getLocation().getY,
      p.getLocation().getZ).toVector.toBlockPoint)
    e.getSlot match {
      case 1 =>
        set.getRegions.forEach(region => {
          regions.removeRegion(region.getId)
          p.sendMessage(AQUA + "保護:" + region.getId + "を削除しました。")
        })
      case 3 => toggleFlag(set,Flags.USE,p)
      case 5 => toggleFlag(set,Flags.INTERACT,p)
      case 7 => toggleFlag(set,Flags.CHEST_ACCESS,p)
      case 19 => toggleFlag(set,Flags.SLEEP,p)
      case 21 => toggleFlag(set,Flags.PLACE_VEHICLE,p)
      case 23 => toggleFlag(set,Flags.DESTROY_VEHICLE,p)
      case _ =>
    }
    new RegionSettingMenu().openMenu(p)
  }

  def toggleFlag(set:ApplicableRegionSet, flag:StateFlag, p:Player): Unit = {
    set.getRegions.forEach(region => {
      if (region.getFlags.getOrDefault(flag,"DENY").toString == "DENY") {
        region.setFlag(flag, StateFlag.State.ALLOW)
        p.sendMessage(s"${AQUA}保護:${region.getId}のフラグ${flag.getName}を有効化しました。")
      } else {
        region.setFlag(flag, StateFlag.State.DENY)
        p.sendMessage(s"${AQUA}保護:${region.getId}のフラグ${flag.getName}を無効化しました。")
      }
    })
  }

}

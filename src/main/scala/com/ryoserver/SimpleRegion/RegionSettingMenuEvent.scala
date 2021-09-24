package com.ryoserver.SimpleRegion

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.WorldGuard
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent

class RegionSettingMenuEvent extends Listener {

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "保護設定メニュー") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    e.getSlot match {
      case 1 =>
        val container = WorldGuard.getInstance().getPlatform.getRegionContainer
        val regions = container.get(BukkitAdapter.adapt(p.getWorld))
        val set = regions.getApplicableRegions(new Location(BukkitAdapter.adapt(p.getWorld),p.getLocation().getX,p.getLocation().getY,
          p.getLocation().getZ).toVector.toBlockPoint)
        set.getRegions.forEach(region => {
          regions.removeRegion(region.getId)
          p.sendMessage(ChatColor.AQUA + "保護:" + region.getId + "を削除しました。")
        })
      case _ =>

    }
  }

}

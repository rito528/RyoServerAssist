package com.ryoserver.World.SimpleRegion

import com.ryoserver.RyoServerAssist
import com.sk89q.worldedit.{IncompleteRegionException, WorldEdit}
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.{ChatColor, Material}


class RegionMenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "保護メニュー") return
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val uuid = p.getUniqueId
    val index = e.getSlot
    index match {
      case 2 =>
        p.getInventory.addItem(new ItemStack(Material.WOODEN_AXE,1))
        p.sendMessage(ChatColor.AQUA + "保護用の木の斧を配布しました。")
      case 4 =>
        if (!ryoServerAssist.getConfig.getStringList("protectionWorlds").contains(p.getWorld.getName.toLowerCase())) {
          p.sendMessage(ChatColor.RED + "このワールドでは保護できません！")
          return
        }
        val container = WorldGuard.getInstance().getPlatform.getRegionContainer
        val regions = container.get(BukkitAdapter.adapt(p.getWorld))
        val session = WorldEdit.getInstance().getSessionManager.get(BukkitAdapter.adapt(p))
        var min:Vector3 = null
        var max:Vector3 = null
        try {
          min = session.getSelection.getMinimumPoint.toVector3.withY(0)
          max = session.getSelection().getMaximumPoint.toVector3.withY(256)
        } catch {
          case e:IncompleteRegionException =>
            p.sendMessage(ChatColor.RED + "保護範囲が指定されていないため、保護できませんでした。")
            return
        }
        var counter = 1
        while (regions.getRegions.containsKey(p.getName + "_" + counter)) {
          counter += 1
        }
        val region = new ProtectedCuboidRegion(p.getName + "_" + counter,min.toBlockPoint,max.toBlockPoint)
        val overlapping = region.getIntersectingRegions(regions.getRegions.values())
        if (overlapping.size() > 0) {
          p.sendMessage(ChatColor.RED + "他の保護範囲と重なっています！")
        } else {
          val owners = region.getOwners
          owners.addPlayer(uuid)
          regions.addRegion(region)
          p.sendMessage(ChatColor.AQUA + "保護が完了しました！")
        }
      case 6 => new RegionSettingMenu().openMenu(p)
      case _ =>
    }
  }

}

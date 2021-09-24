package com.ryoserver.SimpleRegion

import com.ryoserver.Inventory.Item.getItem
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.WorldGuard
import org.bukkit.{Bukkit, ChatColor, Material}
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class RegionSettingMenu {

  def openMenu(p:Player): Unit = {
    val container = WorldGuard.getInstance().getPlatform.getRegionContainer
    val regions = container.get(BukkitAdapter.adapt(p.getWorld))
    val set = regions.getApplicableRegions(new Location(BukkitAdapter.adapt(p.getWorld),p.getLocation().getX,p.getLocation().getY,
      p.getLocation().getZ).toVector.toBlockPoint)
    if (set.size() == 0) {
      p.sendMessage(ChatColor.RED + "この場所は保護されていません！")
      return
    }
    var isOwner = false
    set.forEach(e => {
      if (e.getOwners.contains(p.getUniqueId)) isOwner = true
    })
    if (!isOwner) {
      p.sendMessage(ChatColor.RED + "あなたの保護範囲ではありません！")
      return
    }
    val inv = Bukkit.createInventory(null,54,"保護設定メニュー")
    inv.setItem(1,getItem(Material.TNT,"保護を削除します。",List("取扱注意！","保護範囲を削除します。").asJava))
    p.openInventory(inv)
  }

}

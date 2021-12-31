package com.ryoserver.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Location, Sound}
import org.jetbrains.annotations.NotNull

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class WorldGuardWrapper {

  private val plugin = WorldGuard.getInstance()

  def isGlobal(@NotNull loc: Location): Boolean = {
    getRegion(loc).isEmpty
  }

  def isProtected(@NotNull loc: Location): Boolean = {
    getRegion(loc).nonEmpty
  }

  def getRegion(@NotNull loc: Location): mutable.Set[ProtectedRegion] = {
    val container = plugin.getPlatform.getRegionContainer.get(BukkitAdapter.adapt(loc.getWorld))
    container.getApplicableRegions(BukkitAdapter.adapt(loc).toVector.toBlockPoint).getRegions.asScala
  }

  def isOwner(@NotNull p: Player, @NotNull loc: Location): Boolean = {
    getRegion(loc).foreach(region =>
      if (region.getOwners.contains(p.getUniqueId)) return true
    )
    false
  }

  def removeRegion(@NotNull p: Player): Unit = {
    plugin.getPlatform.getRegionContainer.get(BukkitAdapter.adapt(p.getWorld)).removeRegion(getRegion(p.getLocation()).head.getId)
  }

  def toggleFlag(region: ProtectedRegion, flag: StateFlag, p: Player): Unit = {
    if (region.getFlags.getOrDefault(flag, "DENY").toString == "DENY") {
      region.setFlag(flag, StateFlag.State.ALLOW)
      p.sendMessage(s"${AQUA}フラグ:${flag.getName}を有効にしました。")
    } else {
      region.setFlag(flag, flag.getDefault)
      p.sendMessage(s"${AQUA}フラグ:${flag.getName}を無効にしました。")
    }
    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
  }

}

package com.ryoserver.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class WorldGuardWrapper {

  private val plugin = WorldGuard.getInstance()

  def getRegion(@NotNull loc:Location): mutable.Set[ProtectedRegion] = {
    val container = plugin.getPlatform.getRegionContainer.get(BukkitAdapter.adapt(loc.getWorld))
    container.getApplicableRegions(BukkitAdapter.adapt(loc).toVector.toBlockPoint).getRegions.asScala
  }

  def isProtected(@NotNull loc:Location): Boolean = {
    getRegion(loc).nonEmpty
  }

  def isOwner(@NotNull p:Player,@NotNull loc:Location): Boolean = {
    getRegion(loc).foreach(region =>
      if (region.getOwners.contains(p.getUniqueId)) return true
    )
    false
  }

}

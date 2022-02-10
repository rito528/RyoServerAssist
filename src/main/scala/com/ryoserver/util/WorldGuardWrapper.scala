package com.ryoserver.util

import com.ryoserver.Config.ConfigData.getConfig
import com.sk89q.worldedit.{IncompleteRegionException, WorldEdit}
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.regions.{ProtectedCuboidRegion, ProtectedRegion}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Location, Sound}

import java.util.UUID
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class WorldGuardWrapper {

  private val plugin = WorldGuard.getInstance()

  def isGlobal(loc: Location): Boolean = {
    getRegion(loc).isEmpty
  }

  def isProtected(loc: Location): Boolean = {
    getRegion(loc).nonEmpty
  }

  def getRegion(loc: Location): mutable.Set[ProtectedRegion] = {
    val container = plugin.getPlatform.getRegionContainer.get(BukkitAdapter.adapt(loc.getWorld))
    container.getApplicableRegions(BukkitAdapter.adapt(loc).toVector.toBlockPoint).getRegions.asScala
  }

  def isOwner(p: Player,loc: Location): Boolean = {
    getRegion(loc).foreach(region =>
      if (region.getOwners.contains(p.getUniqueId)) return true
    )
    false
  }

  def getOwner(loc: Location): String = {
    getRegion(loc).head.getOwners.toPlayersString.replaceAll("uuid:", "").split(",")
      .toList
      .map(uuid => Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName)
      .mkString(",")
  }

  def removeRegion(p: Player): Unit = {
    plugin.getPlatform.getRegionContainer.get(BukkitAdapter.adapt(p.getWorld)).removeRegion(getRegion(p.getLocation()).head.getId)
  }

  def flagCheck(region: ProtectedRegion, flag: StateFlag): Boolean = {
    region.getFlags.getOrDefault(flag, "DENY").toString == "ALLOW"
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

  def createRegion(p: Player): Unit = {
    val uuid = p.getUniqueId.toString
    if (!getConfig.protectionWorlds.contains(p.getWorld.getName.toLowerCase())) {
      p.sendMessage(s"${RED}このワールドでは保護できません！")
      return
    }
    val container = WorldGuard.getInstance().getPlatform.getRegionContainer
    val regions = container.get(BukkitAdapter.adapt(p.getWorld))
    val session = WorldEdit.getInstance().getSessionManager.get(BukkitAdapter.adapt(p))
    var min: Vector3 = null
    var max: Vector3 = null
    try {
      min = session.getSelection.getMinimumPoint.toVector3.withY(0)
      max = session.getSelection().getMaximumPoint.toVector3.withY(256)
    } catch {
      case e: IncompleteRegionException =>
        p.sendMessage(s"${RED}保護範囲が指定されていないため、保護できませんでした。")
        return
    }
    var counter = 1
    while (regions.getRegions.containsKey(p.getName.toLowerCase() + "_" + counter)) {
      counter += 1
    }
    val region = new ProtectedCuboidRegion(p.getName.toLowerCase() + "_" + counter, min.toBlockPoint, max.toBlockPoint)
    val overlapping = region.getIntersectingRegions(regions.getRegions.values())
    if (overlapping.size() > 0) {
      p.sendMessage(s"${RED}他の保護範囲と重なっています！")
    } else {
      val owners = region.getOwners
      owners.addPlayer(UUID.fromString(uuid))
      regions.addRegion(region)
      p.sendMessage(s"${AQUA}保護が完了しました！")
      p.playSound(p.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    }
  }

}

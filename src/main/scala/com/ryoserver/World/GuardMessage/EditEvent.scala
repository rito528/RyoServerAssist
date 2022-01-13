package com.ryoserver.World.GuardMessage

import com.ryoserver.util.WorldGuardWrapper
import org.bukkit.event.block.{BlockBreakEvent, BlockPlaceEvent}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EditEvent extends Listener {

  private val protectedWorlds = Set(
    "world",
    "world_nether",
    "world_the_end"
  )

  @EventHandler
  def breakEvent(e: BlockBreakEvent): Unit = {
    val p = e.getPlayer
    val worldGuardWrapper = new WorldGuardWrapper
    val blockLoc = e.getBlock.getLocation
    val worldName = blockLoc.getWorld.getName
    if (protectedWorlds.contains(worldName) && worldGuardWrapper.isGlobal(blockLoc)) {
      p.sendMessage(s"${RED}このブロックは保護されていないためブロックを破壊することができません。")
      p.sendMessage(s"$RED${BOLD}この範囲は保護をすることでブロックの破壊ができるようになります。")
    } else if (protectedWorlds.contains(worldName) && worldGuardWrapper.isProtected(blockLoc) && !worldGuardWrapper.isOwner(p,blockLoc)) {
      p.sendMessage(s"${RED}この範囲は${worldGuardWrapper.getOwner(blockLoc)}によって保護されているためブロックを破壊することができません。")
    }
  }

  @EventHandler
  def placeBlockEvent(e: BlockPlaceEvent): Unit = {
    val p = e.getPlayer
    val worldGuardWrapper = new WorldGuardWrapper
    val blockLoc = e.getBlock.getLocation
    val worldName = blockLoc.getWorld.getName
    if (protectedWorlds.contains(worldName) && worldGuardWrapper.isGlobal(blockLoc)) {
      p.sendMessage(s"${RED}このブロックは保護されていないためブロックを設置することができません。")
      p.sendMessage(s"$RED${BOLD}この範囲は保護をすることでブロックの設置ができるようになります。")
    } else if (protectedWorlds.contains(worldName) && worldGuardWrapper.isProtected(blockLoc) && !worldGuardWrapper.isOwner(p,blockLoc)) {
      p.sendMessage(s"${RED}この範囲は${worldGuardWrapper.getOwner(blockLoc)}によって保護されているためブロックを設置することができません。")
    }
  }

}

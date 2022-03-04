package com.ryoserver.Security

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.{PlayerCommandPreprocessEvent, PlayerMoveEvent}
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.bukkit.scheduler.BukkitRunnable

class SecurityEvent(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  def onMove(e: PlayerMoveEvent): Unit = {
    val p = e.getPlayer
    if (Players.freezeList.contains(p.getName)) {
      e.setCancelled(true)
      p.sendMessage("あなたの動作は禁止されています！")
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  def playerCommandProcess(e: PlayerCommandPreprocessEvent): Unit = {
    val cmd = e.getMessage.split(" ")(0)
    if (!e.getPlayer.isOp && !e.getPlayer.hasPermission("ryoserverassist.commandExecuter") && !getConfig.enableCommands.contains(cmd)) {
      e.getPlayer.sendMessage(s"${RED}このコマンドを実行できません！")
      e.setCancelled(true)
    }
  }

  @EventHandler
  def onEntitySummon(e: CreatureSpawnEvent): Unit = {
    if (!getConfig.worldDoNotProtection.contains(e.getLocation.getWorld.getName)) {
      if (e.getEntity.getType == EntityType.WITHER) {
        e.setCancelled(true)
      } else if (e.getEntity.getType == EntityType.ENDER_DRAGON) {
        new BukkitRunnable {
          override def run(): Unit = {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender, "kill @e[type=minecraft:ender_dragon]")
          }
        }.runTaskLater(ryoServerAssist, 60)
      }
    }
  }

}

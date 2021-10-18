package com.ryoserver.Security

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.{PlayerCommandPreprocessEvent, PlayerJoinEvent, PlayerMoveEvent}
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.bukkit.{Bukkit, ChatColor}

class SecurityEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  def onJoin(e: PlayerJoinEvent): Unit = {
    val checkVPN = new CheckVPNAndProxy
    val p = e.getPlayer
    //VPN検出
    if (!Config.isExclusionUsers(p) && !checkVPN.loginCheck(p,ryoServerAssist)) {
      checkVPN.notification(p.getName)
      p.kickPlayer("[VPN,Proxy検知]\nVPN、Proxyを検知しました。\nVPNやProxyを利用してりょう鯖にアクセスすることはできません。")
    }
    //hide系
    players.hideList.foreach(HP => {
      p.hidePlayer(ryoServerAssist,HP)
    })
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  def onMove(e: PlayerMoveEvent): Unit = {
    val p = e.getPlayer
    if (players.freezeList.contains(p.getName)) {
      e.setCancelled(true)
      p.sendMessage("あなたの動作は禁止されています！")
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  def playerCommandProcess(e: PlayerCommandPreprocessEvent): Unit = {
    val cmd = e.getMessage.split(" ")(0)
    if (!e.getPlayer.isOp && !Config.getBanCommands(cmd)) {
      e.getPlayer.sendMessage(ChatColor.RED + "このコマンドを実行できません！")
      e.setCancelled(true)
    }
  }

  @EventHandler
  def onEntitySummon(e: CreatureSpawnEvent): Unit = {
    if (!Config.getDoNotProtectionWorld(e.getLocation.getWorld.getName)) {
      if (e.getEntity.getType == EntityType.WITHER) {
        e.setCancelled(true)
      } else if (e.getEntity.getType == EntityType.ENDER_DRAGON) {
        e.setCancelled(true)
      }
    }
  }

}

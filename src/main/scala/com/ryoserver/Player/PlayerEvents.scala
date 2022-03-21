package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class PlayerEvents(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    new PlayerLoader().load(e.getPlayer)
  }

  @EventHandler
  def onQuit(e: PlayerQuitEvent): Unit = {
    new PlayerLoader().load(e.getPlayer)
  }

}

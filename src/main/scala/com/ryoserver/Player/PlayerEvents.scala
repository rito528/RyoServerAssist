package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.giveTitle
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class PlayerEvents(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val p = e.getPlayer
    new playerDataLoader(ryoServerAssist).load(p)
    new giveTitle(ryoServerAssist).continuousLogin(p)
  }

  @EventHandler
  def onQuit(e: PlayerQuitEvent): Unit = {
    new playerDataLoader(ryoServerAssist).unload(e.getPlayer)
  }

}

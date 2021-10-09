package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.TitleData.continuousLogin
import com.ryoserver.Title.giveTitle
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class PlayerEvents(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val p = e.getPlayer
    new playerDataLoader(ryoServerAssist).load(p)
    val title = new giveTitle(ryoServerAssist)
    title.continuousLogin(p)
    title.loginDays(p)
    title.loginYear(p)
  }

  @EventHandler
  def onQuit(e: PlayerQuitEvent): Unit = {
    new playerDataLoader(ryoServerAssist).unload(e.getPlayer)
  }

}

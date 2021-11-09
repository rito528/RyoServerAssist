package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.TitleData.continuousLogin
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.SQL
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class PlayerEvents(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val p = e.getPlayer
    new PlayerDataLoader(ryoServerAssist).load(p)
    val title = new GiveTitle(ryoServerAssist)
    title.continuousLogin(p)
    title.loginDays(p)
    title.loginYear(p)
    title.loginPeriod(p)
    title.loginDay(p)
    title.continuousLoginAndQuestClearNumber(p)
  }

  @EventHandler
  def onQuit(e: PlayerQuitEvent): Unit = {
    new PlayerDataLoader(ryoServerAssist).unload(e.getPlayer)
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET lastLogout=NOW() WHERE UUID='${e.getPlayer.getUniqueId.toString}'")
    sql.close()
  }

}

package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.SQL
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class PlayerEvents(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val p = e.getPlayer
    new PlayerDataLoader().load(p)
    val title = new GiveTitle()
    title.continuousLogin(p)
    title.loginDays(p)
    title.loginYear(p)
    title.loginPeriod(p)
    title.loginDay(p)
    title.continuousLoginAndQuestClearNumber(p)
  }

  @EventHandler
  def onQuit(e: PlayerQuitEvent): Unit = {
    new PlayerDataLoader().unload(e.getPlayer)
    val sql = new SQL()
    sql.executeSQL(s"UPDATE Players SET lastLogout=NOW() WHERE UUID='${e.getPlayer.getUniqueId.toString}'")
    sql.close()
  }

}

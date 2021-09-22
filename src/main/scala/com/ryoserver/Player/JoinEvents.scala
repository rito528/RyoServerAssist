package com.ryoserver.Player

import com.ryoserver.Level.Player.LevelLoader
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvents(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val createData = new createData(ryoServerAssist)
    createData.createPlayerData(e.getPlayer)
    new UpdateData(ryoServerAssist).update()
    new LevelLoader(ryoServerAssist).loadPlayerLevel(e.getPlayer)
    SkillPointBer.create(e.getPlayer,ryoServerAssist)
  }

}

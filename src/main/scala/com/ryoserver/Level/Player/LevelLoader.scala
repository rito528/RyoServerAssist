package com.ryoserver.Level.Player

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class LevelLoader(ryoServerAssist: RyoServerAssist) {

  def loadPlayerLevel(p:Player): Unit = {
    val playerExp = new getPlayerData(ryoServerAssist).getPlayerExp(p)
    BossBar.createLevelBer(ryoServerAssist,playerExp, p)
  }

}

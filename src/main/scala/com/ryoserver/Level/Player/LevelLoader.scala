package com.ryoserver.Level.Player

import org.bukkit.entity.Player

class LevelLoader {

  def loadPlayerLevel(p: Player): Unit = {
    val playerExp = new GetPlayerData().getPlayerExp(p)
    BossBar.createLevelBer(playerExp, p)
  }

}

package com.ryoserver.Level.Player

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player

class LevelLoader {

  def loadPlayerLevel(p: Player): Unit = {
    val playerExp = p.getQuestExp
    BossBar.createLevelBer(playerExp, p)
  }

}

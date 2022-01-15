package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import org.bukkit.entity.Player

class SkillPointConsumption {

  def consumption(skillPoint: Double, p: Player): Unit = {
    val playerSP = p.getSkillPoint
    if (skillPoint <= playerSP) {
      p.setSkillPoint(playerSP - skillPoint)
    }
  }

}

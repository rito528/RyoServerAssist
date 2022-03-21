package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player

class SkillPointConsumption {

  def consumption(skillPoint: Double, p: Player): Unit = {
    val playerSP = p.getRyoServerData.skillPoint
    if (skillPoint <= playerSP) {
      p.getRyoServerData.setSkillPoint(playerSP - skillPoint).apply(p)
      SkillPointBer.update(p)
    }
  }

}

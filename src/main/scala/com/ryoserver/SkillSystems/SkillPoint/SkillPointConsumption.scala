package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player

class SkillPointConsumption {

  def consumption(skillPoint: Double, p: Player): Unit = {
    val spData = new SkillPointData()
    val playerSP = p.getSkillPoint
    if (skillPoint <= playerSP) {
      spData.setSkillPoint(p, playerSP - skillPoint)
      SkillPointBer.update(p)
    }
  }

}

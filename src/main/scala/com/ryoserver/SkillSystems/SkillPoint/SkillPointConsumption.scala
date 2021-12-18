package com.ryoserver.SkillSystems.SkillPoint

import org.bukkit.entity.Player

class SkillPointConsumption {

  def consumption(skillPoint: Double, p: Player): Unit = {
    val spData = new SkillPointData()
    val playerSP = spData.getSkillPoint(p)
    if (skillPoint <= playerSP) {
      spData.setSkillPoint(p, (playerSP - skillPoint))
      SkillPointBer.update(p)
    }
  }

}

package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class SkillPointConsumption(ryoServerAssist: RyoServerAssist) {

  def consumption(skillPoint: Int, p: Player): Unit = {
    val spData = new SkillPointData(ryoServerAssist)
    val playerSP = spData.getSkillPoint(p)
    if (skillPoint <= playerSP) {
      spData.setSkillPoint(p, (playerSP - skillPoint))
      SkillPointBer.update(p, ryoServerAssist)
    }
  }

}

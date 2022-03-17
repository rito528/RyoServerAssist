package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player

class SkillPointData() {

  def setSkillPoint(p: Player, newSkillPoint: Double): Unit = {
    p.getRyoServerData.setSkillPoint(newSkillPoint).apply(p)
    SkillPointBer.update(p)
  }

}

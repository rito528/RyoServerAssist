package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.Data
import org.bukkit.entity.Player

class SkillPointData() {

  def getSkillPoint(p: Player): Double = {
    Data.playerData(p.getUniqueId).skillPoint
  }

  def setSkillPoint(p: Player, skillPoint: Double): Unit = {
    val oldPlayerData = Data.playerData(p.getUniqueId)
    Data.playerData = Data.playerData.filterNot { case (uuid, _) => uuid == p.getUniqueId }
    Data.playerData += (p.getUniqueId -> oldPlayerData.copy(skillPoint = skillPoint))
  }

}

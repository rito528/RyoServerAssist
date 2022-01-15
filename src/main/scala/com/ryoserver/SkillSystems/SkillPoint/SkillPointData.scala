package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerData
import org.bukkit.entity.Player

class SkillPointData() {

  def setSkillPoint(p: Player, newSkillPoint: Double): Unit = {
    val oldPlayerData = PlayerData.playerData(p.getUniqueId)
    PlayerData.playerData = PlayerData.playerData.filterNot { case (uuid, _) => uuid == p.getUniqueId }
    PlayerData.playerData += (p.getUniqueId -> oldPlayerData.copy(skillPoint = newSkillPoint))
    SkillPointBer.update(p)
  }

}

package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.{Data, PlayerData}
import org.bukkit.entity.Player

class SkillPointData() {

  def getSkillPoint(p: Player): Double = {
    Data.playerData(p.getUniqueId.toString).skillPoint
  }

  def setSkillPoint(p: Player, skillPoint: Double): Unit = {
    val oldPlayerData = Data.playerData(p.getUniqueId.toString)
    Data.playerData = Data.playerData.filterNot{case (uuid,_) => uuid == p.getUniqueId.toString}
    Data.playerData += (p.getUniqueId.toString -> PlayerData(
      oldPlayerData.level,
      oldPlayerData.exp,
      skillPoint,
      oldPlayerData.ranking,
      oldPlayerData.loginNumber,
      oldPlayerData.consecutiveLoginDays,
      oldPlayerData.questClearTimes,
      oldPlayerData.gachaPullNumber,
      oldPlayerData.voteNumber,
      oldPlayerData.specialSkillOpenPoint,
      oldPlayerData.OpenedSpecialSkills
    ))
  }

}

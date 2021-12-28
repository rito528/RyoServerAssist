package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Level.Player.GetPlayerData
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.{EventHandler, Listener}

class RecoverySkillPointEvent extends Listener {

  @EventHandler
  def onEat(e: PlayerItemConsumeEvent): Unit = {
    val item = e.getItem
    item.setAmount(1)
    val p = e.getPlayer
    if (item == RecoveryItems.min) {
      val skillPointData = new SkillPointData()
      val playerLevel = new GetPlayerData().getPlayerLevel(p)
      val playerSP = skillPointData.getSkillPoint(p)
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      if (playerSP + 300 >= maxSP) skillPointData.setSkillPoint(p, maxSP)
      else skillPointData.setSkillPoint(p, playerSP + 300)
    } else if (item == RecoveryItems.mid) {
      val skillPointData = new SkillPointData()
      val playerLevel = new GetPlayerData().getPlayerLevel(p)
      val playerSP = skillPointData.getSkillPoint(p)
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      if (playerSP + 3000 >= maxSP) skillPointData.setSkillPoint(p, maxSP)
      else skillPointData.setSkillPoint(p, playerSP + 3000)
    } else if (item == RecoveryItems.max) {
      val skillPointData = new SkillPointData()
      val playerLevel = new GetPlayerData().getPlayerLevel(p)
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      skillPointData.setSkillPoint(p, maxSP)
    }
    SkillPointBer.update(p)
  }

}

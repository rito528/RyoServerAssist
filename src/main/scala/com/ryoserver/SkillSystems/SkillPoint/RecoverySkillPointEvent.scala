package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.Item
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.{EventHandler, Listener}

class RecoverySkillPointEvent extends Listener {

  @EventHandler
  def onEat(e: PlayerItemConsumeEvent): Unit = {
    val item = Item.getOneItemStack(e.getItem)
    val p = e.getPlayer
    if (item == RecoveryItems.min) {
      val skillPointData = new SkillPointData()
      val playerLevel = p.getQuestLevel
      val playerSP = p.getSkillPoint
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      if (playerSP + 300 >= maxSP) skillPointData.setSkillPoint(p, maxSP)
      else skillPointData.setSkillPoint(p, playerSP + 300)
    } else if (item == RecoveryItems.mid) {
      val skillPointData = new SkillPointData()
      val playerLevel = p.getQuestLevel
      val playerSP = p.getSkillPoint
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      if (playerSP + 3000 >= maxSP) skillPointData.setSkillPoint(p, maxSP)
      else skillPointData.setSkillPoint(p, playerSP + 3000)
    } else if (item == RecoveryItems.max) {
      val skillPointData = new SkillPointData()
      val playerLevel = p.getQuestLevel
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      skillPointData.setSkillPoint(p, maxSP)
    }
  }

}

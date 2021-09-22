package com.ryoserver.Skill.SkillPoint

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.{EventHandler, Listener}

class RecoverySkillPointEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onEat(e: PlayerItemConsumeEvent): Unit = {
    val item = e.getItem
    item.setAmount(1)
    val p = e.getPlayer
    if (item == RecoveryItems.min) {
      val skillPointData = new SkillPointData(ryoServerAssist)
      val playerLevel = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
      val playerSP = skillPointData.getSkillPoint(p)
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      if (playerSP + 300 >= maxSP) skillPointData.setSkillPoint(p,maxSP)
      else skillPointData.setSkillPoint(p,playerSP + 300)
    } else if (item == RecoveryItems.max) {
      val skillPointData = new SkillPointData(ryoServerAssist)
      val playerLevel = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
      val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
      skillPointData.setSkillPoint(p,maxSP)
    }
  }

}

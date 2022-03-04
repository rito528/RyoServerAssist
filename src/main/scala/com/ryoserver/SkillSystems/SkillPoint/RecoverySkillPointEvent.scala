package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.util.Item
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.{EventHandler, Listener}

class RecoverySkillPointEvent extends Listener {

  @EventHandler
  def onEat(e: PlayerItemConsumeEvent): Unit = {
    val item = Item.getOneItemStack(e.getItem)
    val p = e.getPlayer
    val playerLevel = p.getQuestLevel
    val playerSP = p.getSkillPoint
    val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
    if (item == RecoveryItems.min) {
      if (playerSP + 300 >= maxSP) setMaxSkillPoint(p)
      else p.setSkillPoint(playerSP + 300)
    } else if (item == RecoveryItems.mid) {
      if (playerSP + 3000 >= maxSP) setMaxSkillPoint(p)
      else p.setSkillPoint(playerSP + 3000)
    } else if (item == RecoveryItems.max) {
      setMaxSkillPoint(p)
    }
  }

  private def setMaxSkillPoint(p: Player): Unit = {
    val playerLevel = p.getQuestLevel
    val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
    p.setSkillPoint(maxSP)
    p.playSound(p.getLocation, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1)
  }

}

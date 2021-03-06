package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.Item
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.{EventHandler, Listener}

class RecoverySkillPointEvent extends Listener {

  @EventHandler
  def onEat(e: PlayerItemConsumeEvent): Unit = {
    val item = Item.getOneItemStack(e.getItem)
    implicit val p: Player = e.getPlayer
    val playerLevel = p.getRyoServerData.level
    val playerSP = p.getRyoServerData.skillPoint
    val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
    if (item == RecoveryItems.min) {
      if (playerSP + 300 >= maxSP) setMaxSkillPoint(p)
      else p.getRyoServerData.setSkillPoint(playerSP + 300).apply
    } else if (item == RecoveryItems.mid) {
      if (playerSP + 3000 >= maxSP) setMaxSkillPoint(p)
      else p.getRyoServerData.setSkillPoint(playerSP + 3000).apply
    } else if (item == RecoveryItems.max) {
      setMaxSkillPoint(p)
    }
    SkillPointBer.update(p)
  }

  private def setMaxSkillPoint(implicit p: Player): Unit = {
    val playerLevel = p.getRyoServerData.level
    val maxSP = new SkillPointCal().getMaxSkillPoint(playerLevel)
    p.getRyoServerData.setSkillPoint(maxSP).apply
    p.playSound(p.getLocation, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1)
  }

}

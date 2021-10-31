package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import org.bukkit.Sound
import org.bukkit.entity.Player

class QuestInventory(ryoServerAssist: RyoServerAssist) {

  def selectInventory(player:Player): Unit = {
    val questData = new QuestData(ryoServerAssist)
    if (questData.getSelectedQuest(player) == null) {
      new SelectQuestInventory(ryoServerAssist).inventory(player)
      player.playSound(player.getLocation,Sound.ENTITY_ARROW_SHOOT,1,1)
    }
    else new QuestProcessInventory(ryoServerAssist).inventory(player)
  }

}

package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import org.bukkit.Sound
import org.bukkit.entity.Player

class QuestMenu(ryoServerAssist: RyoServerAssist) {

  def selectInventory(player: Player): Unit = {
    val questData = new QuestData(ryoServerAssist)
    if (questData.getSelectedQuest(player) == null) {
      new SelectQuestMenu(ryoServerAssist).inventory(player,1)
      player.playSound(player.getLocation, Sound.ITEM_BOOK_PAGE_TURN, 1, 1)
    }
    else new QuestProcessMenu(ryoServerAssist).inventory(player)
  }

}

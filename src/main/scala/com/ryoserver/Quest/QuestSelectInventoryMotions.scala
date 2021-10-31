package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import org.bukkit.Sound
import org.bukkit.entity.Player

class QuestSelectInventoryMotions(ryoServerAssist: RyoServerAssist) {

  def Select(p:Player,index:Int): Unit = {
    val questData = new QuestData(ryoServerAssist)
    val questNames = questData.loadQuest(p)
    val lottery = new LotteryQuest()
    lottery.questName = questNames(index)
    lottery.loadQuestData()
    questData.selectQuest(p, lottery)
    p.playSound(p.getLocation,Sound.UI_BUTTON_CLICK,1,1)
    new QuestInventory(ryoServerAssist).selectInventory(p)
  }

  def resetQuest(p:Player): Unit = {
    val questData = new QuestData(ryoServerAssist)
    questData.resetQuest(p)
    new QuestInventory(ryoServerAssist).selectInventory(p)
    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
  }

}

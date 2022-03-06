package com.ryoserver.Quest

import org.bukkit.entity.Player

class QuestGateway(p: Player) {

  def getSelectedQuest: Option[String] = {
    playerQuestData.selectedQuest
  }

  def getSelectedQuestData: QuestDataContext = {
    QuestData.loadedQuestData.filter(_.questName == getSelectedQuest.get).head
  }

  def getSelectedDailyQuest: Option[String] = {
    playerDailyQuestData.selectedQuest
  }

  def getSelectedDailyQuestData: QuestDataContext = {
    QuestData.loadedDailyQuestData.filter(_.questName == getSelectedDailyQuest.get).head
  }
}

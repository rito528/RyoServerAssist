package com.ryoserver.Quest

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util.Date

class QuestGateway(p: Player) {
  private lazy val uuid = p.getUniqueId
  private lazy val playerLevel = p.getQuestLevel
  private lazy val playerQuestData = QuestPlayerData.getPlayerQuestContext(uuid)
  private lazy val playerDailyQuestData = QuestPlayerData.getPlayerDailyQuestContext(uuid)


  def getCanDailyQuests: Set[QuestDataContext] = {
    QuestData.loadedDailyQuestData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

  def selectDailyQuest(questName: String): Unit = {
    QuestPlayerData.setDailyQuestData(uuid,playerDailyQuestData
      .setSelectedQuest(Option(questName))
      .setProgress(Option(QuestData.loadedDailyQuestData
        .filter(_.questName == questName)
        .head
        .requireList)
      )
    )
  }


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

  def questClear(): Unit = {
    new UpdateLevel().addExp(getSelectedQuestData.exp,p)
    p.addQuestClearTimes()
    questDestroy()
  }

  def questDestroy(): Unit = {
    QuestPlayerData.setQuestData(uuid,playerQuestData.setSelectedQuest(None).setProgress(None))
  }

  def dailyQuestClear(ratio: Double = 1.0): Unit = {
    new UpdateLevel().addExp(getSelectedDailyQuestData.exp * ratio,p)
    QuestPlayerData.setLastDailyQuest(uuid,new Date())
    p.addQuestClearTimes()
    dailyQuestDestroy()
  }

  def dailyQuestDestroy(): Unit = {
    QuestPlayerData.setDailyQuestData(uuid,playerDailyQuestData.setSelectedQuest(None).setProgress(None))
  }

}

package com.ryoserver.Quest

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Quest.LoadQuests.loadedQuests
import com.ryoserver.Quest.PlayerQuestData.playerQuestData
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class QuestGateway {

  def selectQuest(p: Player, questName: String): Unit = {
    playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(questName), loadedQuests.filter(_.questName == questName).head.requireList))
  }

  def resetQuest(p: Player): Unit = {
    playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty))
  }

  def getQuestProgress(p: Player): Map[String, Int] = {
    playerQuestData(p.getUniqueId).progress
  }

  def setQuestProgress(p: Player, progress: Map[String, Int]): Unit = {
    getSelectedQuest(p) match {
      case Some(selectedQuest) =>
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(selectedQuest.questName), progress))
      case None =>
    }
  }

  def getCanQuests(lv: Int): List[QuestType] = {
    LoadQuests.loadedQuests.filter(data => data.minLevel <= lv && data.maxLevel >= lv)
  }

  def questClear(p: Player, ryoServerAssist: RyoServerAssist): Unit = {
    getSelectedQuest(p) match {
      case Some(selectedQuest) =>
        new UpdateLevel(ryoServerAssist).addExp(selectedQuest.exp, p)
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty))
      case None =>
    }
  }

  def getSelectedQuest(p: Player): Option[QuestType] = {
    playerQuestData(p.getUniqueId).selectedQuestName match {
      case Some(questName) =>
        Option(loadedQuests.filter(_.questName == questName).head)
      case None =>
        None
    }
  }

}

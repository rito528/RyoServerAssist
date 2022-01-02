package com.ryoserver.Quest

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.Data
import com.ryoserver.Quest.LoadQuests.loadedQuests
import com.ryoserver.Quest.PlayerQuestData.playerQuestData
import com.ryoserver.RyoServerAssist
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestGateway {

  def selectQuest(p: Player, questName: String): Unit = {
    playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(questName), loadedQuests.filter(_.questName == questName).head.requireList,playerQuestData(p.getUniqueId).bookmarks))
  }

  def resetQuest(p: Player): Unit = {
    playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty,playerQuestData(p.getUniqueId).bookmarks))
  }

  def getQuestProgress(p: Player): Map[String, Int] = {
    playerQuestData(p.getUniqueId).progress
  }

  def setQuestProgress(p: Player, progress: Map[String, Int]): Unit = {
    getSelectedQuest(p) match {
      case Some(selectedQuest) =>
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(selectedQuest.questName), progress,playerQuestData(p.getUniqueId).bookmarks))
      case None =>
    }
  }

  def getCanQuests(lv: Int): List[QuestType] = {
    LoadQuests.loadedQuests.filter(data => data.minLevel <= lv && data.maxLevel >= lv)
  }

  def nowNeoStackCanQuest(p:Player,ryoServerAssist: RyoServerAssist): List[QuestType] = {
    val neoStackGateway = new NeoStackGateway(ryoServerAssist)
    getCanQuests(Data.playerData(p.getUniqueId).level)
      .filter(_.questType == "delivery") //neoStackからできるクエストのソートということはすべて納品クエストのはず
      .filter(data => data.requireList
        .forall{requires => neoStackGateway.getNeoStackAmount(p,new ItemStack(Material.matchMaterial(requires._1))) >= requires._2}
      )
  }

  def getBookmarkCanQuest(p:Player): List[QuestType] = {
    //できるクエストとbookmarkされているクエストの積集合を取る
    getCanQuests(Data.playerData(p.getUniqueId).level).intersect(playerQuestData(p.getUniqueId).bookmarks)
  }

  def questClear(p: Player, ryoServerAssist: RyoServerAssist): Unit = {
    getSelectedQuest(p) match {
      case Some(selectedQuest) =>
        new UpdateLevel(ryoServerAssist).addExp(selectedQuest.exp, p)
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty,playerQuestData(p.getUniqueId).bookmarks))
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

package com.ryoserver.Quest

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestGateway {

  def getQuests(sortType: QuestSortContext,p: Player): Set[QuestDataContext] = {
    val playerLevel = p.getQuestLevel
    val canQuests = getCanQuests(playerLevel)
    sortType match {
      case QuestSortContext.normal =>
        //自分のできるクエストすべてを返す
        canQuests
      case QuestSortContext.neoStack =>
        //NeoStackからできるクエストということはすべて納品クエストのはず
        val neoStackGateway = new NeoStackGateway
        canQuests
            .filter(questData => questData.questType == QuestType.delivery && questData.requireList
            .forall{requires => neoStackGateway.getNeoStackAmount(p,new ItemStack(requires._1.material,1)) >= requires._2})
      case QuestSortContext.bookMark =>
        //できるクエストとbookmarkされているクエストの積集合を取る
        canQuests.intersect((if (QuestPlayerData.playerQuestData.contains(p.getUniqueId)) QuestPlayerData.playerQuestData(p.getUniqueId)
          .bookmarks else List.empty)
          .map(
            questName => QuestData.loadedQuestData.filter(_.questName == questName).head
          ).toSet
        )
    }
  }

  private def getCanQuests(playerLevel: Int): Set[QuestDataContext] = {
    QuestData.loadedQuestData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

  def selectQuest(p: Player,questName: String): Unit = {
    QuestPlayerData.playerQuestData += p.getUniqueId -> QuestPlayerData.playerQuestData(p.getUniqueId).setSelectedQuest(Option(questName))
  }

  /*
    クエスト名を指定するとbookmarkに追加・削除をします。
    追加するとtrue、削除されるとfalseを返します。
   */
  def setBookmark(p: Player,questName: String): Boolean = {
    if (QuestPlayerData.playerQuestData.contains(p.getUniqueId)) {
      val playerData = QuestPlayerData.playerQuestData(p.getUniqueId)
      if (playerData.bookmarks.contains(questName)) {
        QuestPlayerData.playerQuestData += p.getUniqueId -> playerData.setBookmarks(playerData.bookmarks.filterNot(_ == questName))
        false
      } else {
        QuestPlayerData.playerQuestData += p.getUniqueId -> playerData.setBookmarks(playerData.bookmarks ++ List(questName))
        true
      }
    } else {
      val progress: Map[MaterialOrEntityType,Int] = Map.empty
      QuestPlayerData.playerQuestData += (p.getUniqueId -> PlayerQuestDataContext(None,Option(progress),List.empty))
      true
    }
  }

  def setQuestSortData(p: Player,sortContext: QuestSortContext): Unit = {
    QuestPlayerData.playerQuestSortData += (p.getUniqueId -> sortContext)
  }

}

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
        canQuests.intersect(QuestPlayerData.getPlayerQuestContext(p.getUniqueId).bookmarks
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
    QuestPlayerData.setQuestData(p.getUniqueId,QuestPlayerData.getPlayerQuestContext(p.getUniqueId).setSelectedQuest(Option(questName)))
  }

  def getSelectedQuest(p: Player): Option[String] = {
    QuestPlayerData.getPlayerQuestContext(p.getUniqueId).selectedQuest
  }

  /*
    クエスト名を指定するとbookmarkに追加・削除をします。
    追加するとtrue、削除されるとfalseを返します。
   */
  def setBookmark(p: Player,questName: String): Boolean = {
    val playerData = QuestPlayerData.getPlayerQuestContext(p.getUniqueId)
    if (playerData.bookmarks.contains(questName)) {
      QuestPlayerData.setQuestData(p.getUniqueId,playerData.setBookmarks(playerData.bookmarks.filterNot(_ == questName)))
      false
    } else {
      QuestPlayerData.setQuestData(p.getUniqueId,playerData.setBookmarks(playerData.bookmarks ++ List(questName)))
      true
    }
  }

  def setQuestSortData(p: Player,sortContext: QuestSortContext): Unit = {
    QuestPlayerData.playerQuestSortData += (p.getUniqueId -> sortContext)
  }

}

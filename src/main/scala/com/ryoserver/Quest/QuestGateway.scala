package com.ryoserver.Quest

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestGateway(p: Player) {
  private lazy val uuid = p.getUniqueId
  private lazy val playerLevel = p.getQuestLevel
  private lazy val playerQuestData = QuestPlayerData.getPlayerQuestContext(uuid)

  def getQuests(sortType: QuestSortContext): Set[QuestDataContext] = {
    val canQuests = getCanQuests
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
        canQuests.intersect(QuestPlayerData.getPlayerQuestContext(uuid).bookmarks
          .map(
            questName => QuestData.loadedQuestData.filter(_.questName == questName).head
          ).toSet
        )
    }
  }

  private def getCanQuests: Set[QuestDataContext] = {
    QuestData.loadedQuestData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

  def selectQuest(questName: String): Unit = {
    QuestPlayerData.setQuestData(uuid,playerQuestData.setSelectedQuest(Option(questName)))
  }

  def getSelectedQuest: Option[String] = {
    playerQuestData.selectedQuest
  }

  /*
    クエスト名を指定するとbookmarkに追加・削除をします。
    追加するとtrue、削除されるとfalseを返します。
   */
  def setBookmark(questName: String): Boolean = {
    if (playerQuestData.bookmarks.contains(questName)) {
      QuestPlayerData.setQuestData(uuid,playerQuestData.setBookmarks(playerQuestData.bookmarks.filterNot(_ == questName)))
      false
    } else {
      QuestPlayerData.setQuestData(uuid,playerQuestData.setBookmarks(playerQuestData.bookmarks ++ List(questName)))
      true
    }
  }

  def setQuestSortData(sortContext: QuestSortContext): Unit = {
    QuestPlayerData.playerQuestSortData += (uuid -> sortContext)
  }

}

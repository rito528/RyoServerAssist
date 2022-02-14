package com.ryoserver.Quest

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestGateway {

  def getQuests(sortType: QuestSortContext,p: Player): Set[QuestDataContext[_]] = {
    val playerLevel = p.getQuestLevel
    val canQuests = getCanQuests(playerLevel)
    sortType match {
      case QuestSortContext.normal =>
        //自分のできるクエストすべてを返す
        canQuests
      case QuestSortContext.bookMark =>
        //できるクエストとbookmarkされているクエストの積集合を取る
        canQuests.intersect(QuestPlayerData.playerQuestData(p.getUniqueId)
          .bookmarks
          .map(
            questName => QuestData.loadedQuestData.filter(_.questName == questName).head).toSet
          )
      case QuestSortContext.neoStack =>
        //NeoStackからできるクエストということはすべて納品クエストのはず
        val neoStackGateway = new NeoStackGateway
        canQuests
            .filter(questData => questData.questType == QuestType.delivery && questData.requireList.asInstanceOf[Map[Material,Int]]
            .forall{requires => neoStackGateway.getNeoStackAmount(p,new ItemStack(requires._1,1)) >= requires._2})
    }
  }

  private def getCanQuests(playerLevel: Int): Set[QuestDataContext[_]] = {
    QuestData.loadedQuestData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

  def selectQuest(p: Player,questName: String): Unit = {
    QuestPlayerData.playerQuestData(p.getUniqueId).setSelectedQuest(Option(questName))
  }

  /*
    クエスト名を指定するとbookmarkに追加・削除をします。
    追加するとtrue、削除されるとfalseを返します。
   */
  def setBookmark(p: Player,questName: String): Boolean = {
    val playerData = QuestPlayerData.playerQuestData(p.getUniqueId)
    if (playerData.bookmarks.contains(questName)) {
      playerData.setBookmarks(playerData.bookmarks.filterNot(_ == questName))
      false
    } else {
      playerData.setBookmarks(playerData.bookmarks ++ List(questName))
      true
    }
  }

}

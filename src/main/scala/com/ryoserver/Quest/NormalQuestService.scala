package com.ryoserver.Quest
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util.UUID

class NormalQuestService(player: Player) extends QuestService {

  private val playerLevel = player.getQuestLevel
  private val uuid = player.getUniqueId
  private val questPlayerData = new QuestPlayerData()

  override val questData: Set[QuestDataContext] = QuestData.loadedQuestData
  override val selectFunc: (UUID, PlayerQuestDataContext) => Unit = questPlayerData.processQuestData.selectQuest
  override val playerQuestDataContext: PlayerQuestDataContext = questPlayerData.getQuestData.getPlayerQuestDataContext(uuid)
  override val p: Player = player

  private def getCanQuests: Set[QuestDataContext] = {
    QuestData.loadedQuestData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

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

  /*
    クエスト名を指定するとbookmarkに追加・削除をします。
    追加するとtrue、削除されるとfalseを返します。
   */
  def setBookmark(questName: String): Boolean = {
    if (playerQuestDataContext.bookmarks.contains(questName)) {
      questPlayerData.processQuestData.selectQuest(
        uuid,
        questPlayerData.getQuestData.getPlayerQuestDataContext(uuid)
          .removeBookmarkQuest(questName)
      )
      false
    } else {
      questPlayerData.processQuestData.selectQuest(
        uuid,
        questPlayerData.getQuestData.getPlayerQuestDataContext(uuid)
          .addBookmarkQuest(questName)
      )
      true
    }
  }

  def setQuestSortData(sortContext: QuestSortContext): Unit = {
    QuestPlayerData.setQuestSortData(uuid,sortContext)
  }

}

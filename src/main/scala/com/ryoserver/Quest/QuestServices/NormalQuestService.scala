package com.ryoserver.Quest.QuestServices

import com.ryoserver.NeoStack.NeoStackService
import com.ryoserver.Quest._
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util.UUID

class NormalQuestService(player: Player) extends QuestService {

  private val uuid = player.getUniqueId
  private val questPlayerData = new QuestPlayerData()

  override val questData: Set[QuestDataContext] = QuestData.loadedQuestData
  override val selectFunc: (UUID, PlayerQuestDataContext) => Unit = questPlayerData.processQuestData.selectQuest
  override val playerQuestDataContext: PlayerQuestDataContext = questPlayerData.getQuestData.getPlayerQuestDataContext(uuid)
  override val p: Player = player

  def getQuests(sortType: QuestSortContext): Set[QuestDataContext] = {
    val canQuests = getCanQuests
    sortType match {
      case QuestSortContext.normal =>
        //自分のできるクエストすべてを返す
        canQuests
      case QuestSortContext.neoStack =>
        //NeoStackからできるクエストということはすべて納品クエストのはず
        val service = new NeoStackService
        canQuests
          .filter(questData => questData.questType == QuestType.delivery && questData.requireList
            .forall{requires => service.getItemAmount(p.getUniqueId,new ItemStack(requires._1.material,1)).getOrElse(0) >= requires._2})
      case QuestSortContext.bookMark =>
        //できるクエストとbookmarkされているクエストの積集合を取る
        canQuests.intersect(new QuestPlayerData().getQuestData.getPlayerQuestContext(uuid).bookmarks
          .map(
            questName => {
              QuestData.loadedQuestData.filter(_.questName == questName).head
            }
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
    new QuestPlayerData().processQuestData.setQuestSortData(uuid,sortContext)
  }

}

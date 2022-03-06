package com.ryoserver.Quest.QuestServices

import com.ryoserver.Quest._
import org.bukkit.entity.Player

import java.util.UUID

class DailyQuestService(player: Player) extends QuestService {

  private val uuid = player.getUniqueId
  private val questPlayerData = new QuestPlayerData()

  override val questData: Set[QuestDataContext] = QuestData.loadedDailyQuestData
  override val selectFunc: (UUID, PlayerQuestDataContext) => Unit = questPlayerData.processQuestData.selectDailyQuest
  override val playerQuestDataContext: PlayerQuestDataContext = questPlayerData.getQuestData.getPlayerDailyQuestContext(uuid)
  override val p: Player = player

}

package com.ryoserver.Quest

import org.bukkit.entity.Player

import java.util.UUID

object QuestSortedData {

  /*
    クエストのソートの保存を行うobject
   */
  private var playerQuestSortData: Map[UUID,QuestSortType] = Map.empty

  def getPlayerQuestSortData(p:Player): QuestSortType = {
    val uuid = p.getUniqueId
    if (playerQuestSortData.contains(uuid)) {
      playerQuestSortData(uuid)
    } else {
      //データが存在しないのでnormalタイプとして取得
      setPlayerQuestSortData(p,QuestSortType.normal)
      QuestSortType.normal
    }
  }

  def setPlayerQuestSortData(p:Player,sortType: QuestSortType): Unit = {
    playerQuestSortData += (p.getUniqueId -> sortType)
  }

}

package com.ryoserver.Quest

import com.ryoserver.Quest.MaterialOrEntityType.{entityType, material}
import com.ryoserver.util.Entity
import org.bukkit.Material
import scalikejdbc.{AutoSession, NoExtractor, SQL, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID
import scala.collection.mutable

object QuestPlayerData {

  private implicit val session: AutoSession.type = AutoSession

  private def getPlayerQuestData(sql: SQL[Nothing,NoExtractor]): mutable.Map[UUID,PlayerQuestDataContext] = {
    mutable.Map() ++ sql.map{rs =>
      val selectedQuest = rs.stringOpt("selectedQuest")
      val remaining = rs.stringOpt("remaining")
      val materialProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        remaining.get.split(";").map { data =>
          val splitData = data.split(":")
          material(Material.matchMaterial(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      val suppressionProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        remaining.get.split(":").map { data =>
          val splitData = data.split(";")
          entityType(Entity.getEntity(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      val bookmarks = rs.stringOpt("bookmarks").getOrElse("").split(";").toList
      if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(materialProgress), bookmarks)
      } else if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(suppressionProgress), bookmarks)
      } else {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(None,Option(materialProgress),bookmarks)
      }
    }.toList().apply().toMap
  }

  private val playerQuestData: mutable.Map[UUID, PlayerQuestDataContext] = getPlayerQuestData(sql"SELECT * FROM Quests")
  private val playerDailyQuestData: mutable.Map[UUID,PlayerQuestDataContext] = getPlayerQuestData(sql"SELECT * FROM DailyQuests")
  private val playerQuestSortData: mutable.Map[UUID,QuestSortContext] = mutable.Map.empty

  def getPlayerQuestContext(uuid: UUID): PlayerQuestDataContext = {
    if (playerQuestData.contains(uuid)) playerQuestData(uuid)
    else PlayerQuestDataContext(None,None,List.empty)
  }

  def setQuestData(uuid: UUID,playerQuestDataContext: PlayerQuestDataContext): Unit = {
    playerQuestData += uuid -> playerQuestDataContext
  }

  def getPlayerDailyQuestContext(uuid: UUID): PlayerQuestDataContext = {
    if (playerDailyQuestData.contains(uuid)) playerDailyQuestData(uuid)
    else PlayerQuestDataContext(None,None,List.empty)
  }

  def setDailyQuestData(uuid: UUID,playerQuestDataContext: PlayerQuestDataContext): Unit = {
    playerDailyQuestData += uuid -> playerQuestDataContext
  }

  def getQuestSortData(uuid: UUID): QuestSortContext = {
    if (playerQuestSortData.contains(uuid)) playerQuestSortData(uuid)
    else QuestSortContext.normal
  }

  def setQuestSortData(uuid: UUID,questSortContext: QuestSortContext): Unit = {
    playerQuestSortData += uuid -> questSortContext
  }


}

package com.ryoserver.Quest

import com.ryoserver.util.Entity
import org.bukkit.Material
import org.bukkit.entity.EntityType
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID
import scala.collection.mutable

object QuestPlayerData {

  private implicit val session: AutoSession.type = AutoSession

  val playerQuestData: mutable.Map[UUID,PlayerQuestDataContext[_]] =
    mutable.Map() ++ sql"SELECT UUID,selectedQuest,remaining,bookmarks FROM Quests"
      .map{rs =>
        val selectedQuest = rs.stringOpt("selectedQuest")
        val remaining = rs.stringOpt("remaining")
        val materialProgress: Map[Material,Int]= if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
          remaining.get.split(";").map { data =>
            val splitData = data.split(":")
            Material.matchMaterial(splitData(0)) -> splitData(1).toInt
          }.toMap
        } else {
         Map.empty
        }
        val suppressionProgress: Map[EntityType,Int] = if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
          remaining.get.split(":").map { data =>
            val splitData = data.split(";")
            Entity.getEntity(splitData(0)) -> splitData(1).toInt
          }.toMap
        } else {
          Map.empty
        }
        if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
          UUID.fromString(rs.string("UUID")) ->
            PlayerQuestDataContext(selectedQuest,Option(materialProgress),
              rs.stringOpt("bookmarks").getOrElse("").split(";").toList).asInstanceOf[PlayerQuestDataContext[_]]
        } else if (selectedQuest.nonEmpty && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
          UUID.fromString(rs.string("UUID")) ->
            PlayerQuestDataContext(selectedQuest,Option(suppressionProgress),
              rs.stringOpt("bookmarks").getOrElse("").split(";").toList).asInstanceOf[PlayerQuestDataContext[_]]
        } else {
          UUID.fromString(rs.string("UUID")) ->
            PlayerQuestDataContext(None,Option(materialProgress),rs.stringOpt("bookmarks").getOrElse("").split(";").toList).asInstanceOf[PlayerQuestDataContext[_]]
        }
      }.toList().apply().toMap

}

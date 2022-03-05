package com.ryoserver.Quest

import com.ryoserver.Quest.MaterialOrEntityType.{entityType, material}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable
import scalikejdbc.{AutoSession, NoExtractor, SQL, scalikejdbcSQLInterpolationImplicitDef}

import java.text.SimpleDateFormat
import java.util.{Date, UUID}
import scala.collection.mutable

object QuestPlayerData {

  private implicit val session: AutoSession.type = AutoSession

  private def getPlayerQuestData(sql: SQL[Nothing,NoExtractor]): mutable.Map[UUID,PlayerQuestDataContext] = {
    mutable.Map() ++ sql.map{rs =>
      val selectedQuest = rs.stringOpt("selectedQuest")
      val remaining = rs.stringOpt("remaining")
      val materialProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.getOrElse("") != "" && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        remaining.get.split(";").map { data =>
          val splitData = data.split(":")
          material(Material.matchMaterial(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      val suppressionProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.getOrElse("") != "" && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        remaining.get.split(":").map { data =>
          val splitData = data.split(";")
          entityType(Entity.getEntity(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      val bookmarks = rs.stringOpt("bookmarks") match {
        case Some(bookmarkData) =>
          bookmarkData.split(";").toList
        case None =>
          List.empty
      }
      if (selectedQuest.getOrElse("") != "" && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(materialProgress), bookmarks)
      } else if (selectedQuest.getOrElse("") != "" && QuestData.loadedQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(suppressionProgress), bookmarks)
      } else {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(None,Option(materialProgress),bookmarks)
      }
    }.toList().apply().toMap
  }

  private def getPlayerDailyQuestData(sql: SQL[Nothing,NoExtractor]): mutable.Map[UUID,PlayerQuestDataContext] = {
    mutable.Map() ++ sql.map{rs =>
      val selectedQuest = rs.stringOpt("selectedQuest")
      val remaining = rs.stringOpt("remaining")
      val materialProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.getOrElse("") != "" && QuestData.loadedDailyQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        remaining.get.split(";").map { data =>
          val splitData = data.split(":")
          material(Material.matchMaterial(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      val suppressionProgress: Map[MaterialOrEntityType,Int] = if (selectedQuest.getOrElse("") != "" && QuestData.loadedDailyQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        remaining.get.split(":").map { data =>
          val splitData = data.split(";")
          entityType(Entity.getEntity(splitData(0))) -> splitData(1).toInt
        }.toMap
      } else {
        Map.empty
      }
      if (selectedQuest.getOrElse("") != "" && QuestData.loadedDailyQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(materialProgress), List.empty)
      } else if (selectedQuest.getOrElse("") != "" && QuestData.loadedDailyQuestData.filter(_.questName == selectedQuest.get).head.questType == QuestType.suppression) {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(selectedQuest,Option(suppressionProgress), List.empty)
      } else {
        UUID.fromString(rs.string("UUID")) ->
          PlayerQuestDataContext(None,Option(materialProgress),List.empty)
      }
    }.toList().apply().toMap
  }

  private val playerQuestData: mutable.Map[UUID, PlayerQuestDataContext] = getPlayerQuestData(sql"SELECT * FROM Quests")
  private val playerDailyQuestData: mutable.Map[UUID,PlayerQuestDataContext] = getPlayerDailyQuestData(sql"SELECT * FROM DailyQuests")
  private val playerQuestSortData: mutable.Map[UUID,QuestSortContext] = mutable.Map.empty
  private val lastDailyQuestDate: mutable.Map[UUID,Date] = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    mutable.Map() ++ sql"SELECT UUID,LastDailyQuest FROM Players".map{rs =>
      UUID.fromString(rs.string("UUID")) -> format.parse(rs.string("LastDailyQuest"))
    }.toList().apply().toMap
  }

  def playerQuestDataSave(): Unit = {
    playerQuestData.foreach{case (uuid,context) =>
      val uuidString = uuid.toString
      val selectedQuest = context.selectedQuest
      val progress: String = selectedQuest match {
        case Some(name) =>
          context.progress.getOrElse(Map.empty).map{case(materialOrEntityType,amount) =>
            if (QuestData.loadedQuestData.exists(_.questName == name)) {
              QuestData.loadedQuestData.filter(_.questName == name).head.questType match {
                case QuestType.delivery =>
                  s"${materialOrEntityType.material.name}:$amount"
                case QuestType.suppression =>
                  s"${materialOrEntityType.entityType.name}:$amount"
              }
            } else {
              ""
            }
          }.mkString(";")
        case None =>
          ""
      }
      val bookmarks = context.bookmarks.mkString(";")
      sql"""INSERT INTO Quests (UUID,selectedQuest,remaining,bookmarks) VALUES ($uuidString,${selectedQuest.getOrElse("")},$progress,$bookmarks)
           ON DUPLICATE KEY UPDATE
           selectedQuest=${selectedQuest.getOrElse("")},
           remaining=$progress,
           bookmarks=$bookmarks""".execute().apply()
    }
  }

  def playerDailyQuestDataSave(): Unit = {
    playerDailyQuestData.foreach{case (uuid,context) =>
      val uuidString = uuid.toString
      val selectedQuest = context.selectedQuest
      val progress: String = selectedQuest match {
        case Some(name) =>
          context.progress.getOrElse(Map.empty).map{case(materialOrEntityType,amount) =>
            QuestData.loadedDailyQuestData.filter(_.questName == name).head.questType match {
              case QuestType.delivery =>
                s"${materialOrEntityType.material.name}:$amount"
              case QuestType.suppression =>
                s"${materialOrEntityType.entityType.name}:$amount"
            }
          }.mkString(";")
        case None =>
          ""
      }
      sql"""INSERT INTO DailyQuests (UUID,selectedQuest,remaining) VALUES ($uuidString,${selectedQuest.getOrElse("")},$progress)
           ON DUPLICATE KEY UPDATE
           selectedQuest=${selectedQuest.getOrElse("")},
           remaining=$progress""".execute().apply()
    }
  }

  def saveLastDailyQuestDate(): Unit = {
    lastDailyQuestDate.foreach{case (uuid,date) =>
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      sql"UPDATE Players SET LastDailyQuest=${simpleDateFormat.format(date.getTime)} WHERE UUID=${uuid.toString}".execute().apply()
    }
  }

  def playerQuestDataAutoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        playerQuestDataSave()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,20 * 60,20 * 60)
  }

  def playerDailyQuestDataAutoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        playerDailyQuestDataSave()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,20 * 60,20 * 60)
  }

  def lastDailyQuestDateAutoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        saveLastDailyQuestDate()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,20 * 60,20 * 60)
  }

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

  def getLastDailyQuest(uuid: UUID): Date = {
    if (lastDailyQuestDate.contains(uuid)) {
      lastDailyQuestDate(uuid)
    } else {
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val date = simpleDateFormat.parse("2000-01-01 00:00:00")
      setLastDailyQuest(uuid,date)
      date
    }
  }

  def setLastDailyQuest(uuid: UUID,date: Date): Unit = {
    lastDailyQuestDate += uuid -> date
  }


}

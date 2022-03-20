package com.ryoserver.Quest

import com.ryoserver.Quest.MaterialOrEntityType.{entityType, material}
import com.ryoserver.Quest.QuestPlayerData.{lastDailyQuestDate, playerDailyQuestData, playerQuestData, playerQuestSortData}
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

  private val playerQuestData: mutable.Map[UUID, PlayerQuestDataContext] = getPlayerQuestData(sql"SELECT * FROM Quests",QuestData.loadedQuestData)
  private val playerDailyQuestData: mutable.Map[UUID,PlayerQuestDataContext] = getPlayerQuestData(sql"SELECT * FROM DailyQuests",QuestData.loadedDailyQuestData,isDaily = true)
  private val playerQuestSortData: mutable.Map[UUID,QuestSortContext] = mutable.Map.empty
  private val lastDailyQuestDate: mutable.Map[UUID,Date] = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    mutable.Map() ++ sql"SELECT uuid,last_daily_quest FROM Players".map{rs =>
      UUID.fromString(rs.string("uuid")) -> format.parse(rs.string("last_daily_quest"))
    }.toList().apply().toMap
  }

  private def getPlayerQuestData(sql: SQL[Nothing,NoExtractor],rawData: Set[QuestDataContext],isDaily: Boolean = false): mutable.Map[UUID, PlayerQuestDataContext] = {
    mutable.Map() ++ sql.map{rs =>
      val uuid = UUID.fromString(rs.string("UUID"))
      val selectedQuest = rs.stringOpt("selectedQuest")
      val remaining = rs.stringOpt("remaining")
      val progress: Map[MaterialOrEntityType,Int] = {
        if (selectedQuest.getOrElse("") != "") {
          remaining.get.split(";").map{data =>
            val splitData = data.split(":")
            splitData(0) -> splitData(1).toInt
          }.toMap.map{case (key,value) =>
            if (rawData.filter(_.questName == selectedQuest.get).head.questType == QuestType.delivery) {
              material(Material.matchMaterial(key)) -> value
            } else {
              entityType(Entity.getEntity(key)) -> value
            }
          }
        } else {
          Map.empty
        }
      }
      val bookmarks = if (!isDaily) {
        rs.stringOpt("bookmarks") match {
          case Some(bookmarkData) =>
            if (bookmarkData != "") {
              bookmarkData.split(";").toList
            } else {
              List.empty
            }
          case None =>
            List.empty
        }
      } else {
        List.empty
      }
      uuid -> PlayerQuestDataContext(selectedQuest,Option(progress),bookmarks)
    }.toList.apply.toMap
  }

}

final class QuestPlayerData {

  final object processQuestData {

    def selectQuest(uuid: UUID,playerQuestDataContext: PlayerQuestDataContext): Unit = {
      playerQuestData += uuid -> playerQuestDataContext
    }

    def selectDailyQuest(uuid: UUID,playerQuestDataContext: PlayerQuestDataContext): Unit = {
      playerDailyQuestData += uuid -> playerQuestDataContext
    }

    def changeLastDailyQuest(uuid: UUID,date: Date): Unit = {
      lastDailyQuestDate += uuid -> date
    }

    def setQuestSortData(uuid: UUID,questSortContext: QuestSortContext): Unit = {
      playerQuestSortData += uuid -> questSortContext
    }

  }

  final object getQuestData {

    def getPlayerQuestDataContext(uuid: UUID): PlayerQuestDataContext = {
      if (playerQuestData.contains(uuid)) playerQuestData(uuid)
      else PlayerQuestDataContext(None,None,List.empty)
    }

    def getPlayerDailyQuestContext(uuid: UUID): PlayerQuestDataContext = {
      if (playerDailyQuestData.contains(uuid)) playerDailyQuestData(uuid)
      else PlayerQuestDataContext(None,None,List.empty)
    }

    def getQuestSortData(uuid: UUID): QuestSortContext = {
      if (playerQuestSortData.contains(uuid)) playerQuestSortData(uuid)
      else QuestSortContext.normal
    }

    def getLastDailyQuest(uuid: UUID): Date = {
      if (lastDailyQuestDate.contains(uuid)) {
        lastDailyQuestDate(uuid)
      } else {
        val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = simpleDateFormat.parse("2000-01-01 00:00:00")
        processQuestData.changeLastDailyQuest(uuid,date)
        date
      }
    }

    def getPlayerQuestContext(uuid: UUID): PlayerQuestDataContext = {
      if (playerQuestData.contains(uuid)) playerQuestData(uuid)
      else PlayerQuestDataContext(None,None,List.empty)
    }

  }

  final object saver {

    private implicit val session: AutoSession.type = AutoSession
    private val oneMinute = 1200

    def savePlayerQuestData(): Unit = {
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

    def autoSavePlayerQuestData(implicit ryoServerAssist: RyoServerAssist): Unit = {
      new BukkitRunnable {
        override def run(): Unit = {
          savePlayerQuestData()
        }
      }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
    }

    def savePlayerDailyQuestData(): Unit = {
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

    def autoSavePlayerDailyQuestData(implicit ryoServerAssist: RyoServerAssist): Unit = {
      new BukkitRunnable {
        override def run(): Unit = {
          savePlayerDailyQuestData()
        }
      }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
    }

    def saveLastDailyQuestDate(): Unit = {
      lastDailyQuestDate.foreach{case (uuid,date) =>
        val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sql"UPDATE Players SET LastDailyQuest=${simpleDateFormat.format(date.getTime)} WHERE UUID=${uuid.toString}".execute().apply()
      }
    }

    def autoSaveLastDailyQuestDate(implicit ryoServerAssist: RyoServerAssist): Unit = {
      new BukkitRunnable {
        override def run(): Unit = {
          saveLastDailyQuestDate()
        }
      }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
    }

  }

}

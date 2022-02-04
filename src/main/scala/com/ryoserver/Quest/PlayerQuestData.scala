package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID

object PlayerQuestData {

  var playerQuestData: Map[UUID, PlayerQuestDataType] = Map.empty

  def loadPlayerData(p: Player): Unit = {
    if (playerQuestData.contains(p.getUniqueId)) return
    implicit val session: AutoSession.type = AutoSession
    val quests = sql"SELECT selectedQuest,remaining,bookmarks FROM Quests WHERE UUID=${p.getUniqueId.toString}"
    val questData = quests.getHeadData
    case class questDataClass(selectedQuest: Option[String],remaining: Option[String],bookmarks: Option[String])
    val data = quests
      .map(rs =>
        questDataClass(
          rs.stringOpt("selectedQuest"),
          rs.stringOpt("remaining"),
          rs.stringOpt("bookmarks"))
      )
      .headOption.apply()
    if (questData.nonEmpty) {
      val selectedQuestName:Option[String] = data.get.selectedQuest
      val remaining = data.get.remaining.orNull
      val bookmarks = data.get.bookmarks.orNull
      val bookMarkData: List[String] = if (bookmarks != null) bookmarks.split(";").toList else Nil
      if (remaining != null) {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(selectedQuestName, remaining
          .split(";")
          .map(data => data.split(":")(0) -> data.split(":")(1).toInt).toMap, bookMarkData))
      } else {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty, bookMarkData))
      }
    } else {
      sql"INSERT INTO Quests (UUID) VALUES (${p.getUniqueId.toString})".execute.apply()
      playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty, List.empty))
    }
  }

  def autoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 1200, 1200)
  }

  def save(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    playerQuestData.foreach { case (uuid, questData) =>
      val questName = questData.selectedQuestName
      questName match {
        case Some(name) =>
          sql"""UPDATE Quests SET selectedQuest=$name,
               remaining=${questData.progress.map { case (require, amount) => s"$require:$amount" }.mkString(";")}
               WHERE UUID=${uuid.toString}
               """.execute.apply()
        case None =>
          sql"""UPDATE Quests SET selectedQuest=NULL,remaining=NULL WHERE UUID=${uuid.toString}""".execute.apply()
      }
      sql"""UPDATE Quests SET bookmarks=${if (questData.bookmarks.isEmpty) "NULL" else questData.bookmarks.mkString(";")}
           WHERE UUID=${uuid.toString}"""
    }
  }

}

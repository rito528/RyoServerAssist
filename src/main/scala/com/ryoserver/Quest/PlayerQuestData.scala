package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

object PlayerQuestData {

  var playerQuestData: Map[UUID, PlayerQuestDataType] = Map.empty

  def loadPlayerData(ryoServerAssist: RyoServerAssist, p: Player): Unit = {
    if (playerQuestData.contains(p.getUniqueId)) return
    new DataBaseTable(ryoServerAssist).createQuestTable()
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT selectedQuest,remaining,bookmarks FROM Quests WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) {
      val selectedQuestName = rs.getString("selectedQuest")
      val remaining = rs.getString("remaining")
      val bookmarks = rs.getString("bookmarks")
      var bookMarkData:List[String] = null
      if (bookmarks != null) {
        bookMarkData = bookmarks.split(";").toList
      } else {
        bookMarkData = List.empty
      }
      if (remaining != null) {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(selectedQuestName), remaining
          .split(";")
          .map(data => data.split(":")(0) -> data.split(":")(1).toInt).toMap,bookMarkData))
      } else {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty,bookMarkData))
      }
    } else {
      sql.executeSQL(s"INSERT INTO Quests (UUID) VALUES ('${p.getUniqueId.toString}')")
      playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty,List.empty))
    }
    sql.close()
  }

  def autoSave(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 1200, 1200)
  }

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    new DataBaseTable(ryoServerAssist).createQuestTable()
    val sql = new SQL(ryoServerAssist)
    playerQuestData.foreach { case (uuid, questData) =>
      val questName = questData.selectedQuestName
      questName match {
        case Some(name) =>
          sql.executeSQL(s"UPDATE Quests SET selectedQuest='$name'," +
            s"remaining='${questData.progress.map { case (require, amount) => s"$require:$amount" }.mkString(";")}' " +
            s"WHERE UUID='${uuid.toString}'")
        case None =>
          sql.executeSQL(s"UPDATE Quests SET selectedQuest=NULL," +
            s"remaining=NULL " +
            s"WHERE UUID='${uuid.toString}'")
      }
      sql.executeSQL(s"UPDATE Quests SET bookmarks='${if (questData.bookmarks.isEmpty) "NULL" else questData.bookmarks.mkString(";")}' WHERE UUID='${uuid.toString}'")
    }
    sql.close()
  }

}

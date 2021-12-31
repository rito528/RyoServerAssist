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
    val rs = sql.executeQuery(s"SELECT selectedQuest,remaining FROM Quests WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) {
      val selectedQuestName = rs.getString("selectedQuest")
      val remaining = rs.getString("remaining")
      if (remaining != null) {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(Option(selectedQuestName), remaining
          .split(";")
          .map(data => data.split(":")(0) -> data.split(":")(1).toInt).toMap))
      } else {
        playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty))
      }
    } else {
      playerQuestData += (p.getUniqueId -> PlayerQuestDataType(None, Map.empty))
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

  private def save(ryoServerAssist: RyoServerAssist): Unit = {
    new DataBaseTable(ryoServerAssist).createQuestTable()
    val sql = new SQL(ryoServerAssist)
    playerQuestData.foreach { case (uuid, questData) =>
      val questName = questData.selectedQuestName
      questName match {
        case Some(name) =>
          sql.executeSQL(s"UPDATE Quests SET selectedQuest='$name'," +
            s"remaining='${questData.progress.map { case (require, amount) => s"$require:$amount" }.mkString(";")}' WHERE UUID='${uuid.toString}'")
        case None =>
          sql.executeSQL(s"UPDATE Quests SET selectedQuest=NULL," +
            s"remaining=NULL WHERE UUID='${uuid.toString}'")
      }
    }
    sql.close()
  }

}

package com.ryoserver.Quest

import com.ryoserver.Level.Player.{getPlayerData, updateLevel}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class QuestData(ryoServerAssist: RyoServerAssist) {

  private def createQuestTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'Quests';")
    if (!checkTable.next()) sql.executeSQL("CREATE TABLE Quests(id INT AUTO_INCREMENT,UUID TEXT,QuestName TEXT,selectedQuest TEXT,remaining TEXT,PRIMARY KEY(id));")
    sql.close()
  }

  def saveQuest(p:Player,quests: Array[String]): Unit = {
    createQuestTable()
    val sql = new SQL(ryoServerAssist)
    val checkPlayerData = sql.executeQuery(s"SELECT UUID FROM Quests WHERE UUID='${p.getUniqueId.toString}';")
    if (!checkPlayerData.next()) sql.executeSQL(s"INSERT INTO Quests (UUID,QuestName) VALUES ('${p.getUniqueId.toString}','${quests.mkString(";")}');")
    else sql.executeSQL(s"UPDATE Quests SET QuestName='${quests.mkString(";")}' WHERE UUID='${p.getUniqueId.toString}';")
  }

  def loadQuest(p:Player): Array[String] = {
    createQuestTable()
    val sql = new SQL(ryoServerAssist)
    val check = sql.executeQuery(s"SELECT * FROM Quests WHERE UUID='${p.getUniqueId.toString}';")
    var quests = Array.empty[String]
    if (!check.next()) return Array.empty[String]
    check.getString("QuestName").split(";").foreach(name => quests :+= name)
    quests
  }

  def selectQuest(p:Player,data: LotteryQuest): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (data.questType == "delivery") {
      sql.executeSQL(s"UPDATE Quests SET selectedQuest='${data.questName}',remaining='${data.items.toArray.mkString(";")}' WHERE UUID='${p.getUniqueId.toString}';")
    } else if (data.questType == "suppression") {
      sql.executeSQL(s"UPDATE Quests SET selectedQuest='${data.questName}',remaining='${data.mobs.toArray.mkString(";")}' WHERE UUID='${p.getUniqueId.toString}';")
    }
    sql.close()
  }

  def resetQuest(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"DELETE FROM Quests WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
  }

  def getSelectedQuest(p:Player): String = {
    createQuestTable()
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT selectedQuest FROM Quests WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) return rs.getString("selectedQuest")
    sql.close()
    null
  }

  def getSelectedQuestRemaining(p:Player): String = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT remaining FROM Quests WHERE UUID='${p.getUniqueId.toString}';")
    if (rs.next()) return rs.getString("remaining")
    sql.close()
    null
  }

  def setSelectedQuestItemRemaining(p:Player,remaining:String): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Quests SET remaining='$remaining' WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
  }

  def questClear(p:Player): Unit = {
    val exp = new getPlayerData(ryoServerAssist).getPlayerExp(p)
    val lottery = new LotteryQuest(ryoServerAssist)
    lottery.questName = getSelectedQuest(p)
    lottery.getQuest()
    new updateLevel(ryoServerAssist).updateExp(exp + lottery.exp,p)
    resetQuest(p)
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET questClearTimes=questClearTimes + 1 WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
  }

}

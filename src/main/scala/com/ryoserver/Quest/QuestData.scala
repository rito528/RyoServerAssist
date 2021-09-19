package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class QuestData(ryoServerAssist: RyoServerAssist) {

  private def createQuestTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'Quests';")
    if (!checkTable.next()) sql.executeSQL("CREATE TABLE Quests(id INT AUTO_INCREMENT,UUID TEXT,QuestName TEXT, PRIMARY KEY(id));")
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
    val sql = new SQL(ryoServerAssist)
    val check = sql.executeQuery(s"SELECT * FROM Quests WHERE UUID='${p.getUniqueId.toString}';")
    var quests = Array.empty[String]
    if (!check.next()) return Array.empty[String]
    check.getString("QuestName").split(";").foreach(name => quests :+= name)
    quests
  }

}

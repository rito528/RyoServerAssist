package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class DataBaseTable(ryoServerAssist: RyoServerAssist) {

  def createQuestTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Quests(id INT AUTO_INCREMENT,UUID TEXT,QuestName TEXT,selectedQuest TEXT,remaining TEXT,PRIMARY KEY(id));")
    sql.close()
  }

}

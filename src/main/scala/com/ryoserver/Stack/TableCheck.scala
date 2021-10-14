package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class TableCheck(ryoServerAssist: RyoServerAssist) {

  def stackTableCheck(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val checkStackTable = sql.executeQuery("SHOW TABLES LIKE 'StackData';")
    if (!checkStackTable.next()) {
      sql.executeSQL("CREATE TABLE StackData(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,amount INT,PRIMARY KEY(`id`))")
    }
    val checkStackList = sql.executeQuery("SHOW TABLES LIKE 'StackList';")
    if (!checkStackList.next()) {
      sql.executeSQL("CREATE TABLE StackList(id INT AUTO_INCREMENT,category TEXT,item TEXT,PRIMARY KEY(`id`));")
    }
    sql.close()
  }

}

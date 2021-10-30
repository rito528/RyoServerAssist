package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class TableCheck(ryoServerAssist: RyoServerAssist) {

  def stackTableCheck(): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL("CREATE TABLE IF NOT EXISTS StackData(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,amount INT,PRIMARY KEY(`id`))")
    sql.executeSQL("CREATE TABLE IF NOT EXISTS StackList(id INT AUTO_INCREMENT,category TEXT,page INT,invItem TEXT,PRIMARY KEY(`id`));")
    sql.close()
  }

}

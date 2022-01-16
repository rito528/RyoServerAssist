package com.ryoserver.DataBase

import com.ryoserver.util
import com.ryoserver.util.{ColumnData, SQL}

class CreateTables {

  private val sql = new SQL()

  /*
    RyoServerAssistで利用するテーブルを作成するためのクラス。
    execute()を実行することによりすべてのテーブルを作成します
   */

  def execute(): Unit = {
    firstJoinItems()
    quests()
    neoStackTables()
    gachaItems()
    distribution()
    homes()
    players()
    events()
    eventRankings()
    storage()
    sql.close()
  }

  private def firstJoinItems(): Unit = {
    sql.createTable("firstJoinItems",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("ItemStack","TEXT",null)
    ))
  }

  private def quests(): Unit = {
    sql.createTable("Quests",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("UUID","TEXT",null),
      ColumnData("selectedQuest","TEXT",null),
      ColumnData("remaining","TEXT",null),
      ColumnData("bookmarks","TEXT",null)
    ))
  }

  private def neoStackTables(): Unit = {
    sql.executeSQL("CREATE TABLE IF NOT EXISTS StackData(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,amount INT,PRIMARY KEY(`id`))")
    sql.executeSQL("CREATE TABLE IF NOT EXISTS StackList(id INT AUTO_INCREMENT,category TEXT,page INT,invItem TEXT,PRIMARY KEY(`id`));")
  }

  private def gachaItems(): Unit = {
    sql.executeSQL("CREATE TABLE IF NOT EXISTS GachaItems(id INT AUTO_INCREMENT,Rarity INT,Material TEXT,PRIMARY KEY(`id`));")
  }

  private def distribution(): Unit = {
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Distribution(id INT AUTO_INCREMENT,GachaPaperType TEXT,Count INT, PRIMARY KEY(id));")
  }

  private def homes(): Unit = {
    sql.executeSQL(s"CREATE TABLE IF NOT EXISTS `Homes`(UUID TEXT,point INT,Location TEXT,Locked BOOLEAN);")
  }

  private def players(): Unit = {
    //UUID=UUID,lastLogin=最終ログイン,loginDays=ログイン日数,consecutiveLoginDays=連続ログイン日数,lastDistributionReceived=最後に受け取った配布番号)
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Players(UUID Text,lastLogin DATETIME,lastLogout DATETIME,loginDays INT,consecutiveLoginDays INT," +
      "lastDistributionReceived INT,EXP DOUBLE,Level INT,questClearTimes INT,gachaTickets INT,gachaPullNumber INT,SkillPoint INT," +
      "SkillOpenPoint INT,OpenedSkills TEXT,OpenedTitles TEXT,SelectedTitle TEXT,autoStack BOOLEAN,VoteNumber INT);")
  }

  private def eventRankings(): Unit = {
    sql.executeSQL(s"CREATE TABLE IF NOT EXISTS EventRankings(UUID TEXT, EventName TEXT, counter INT)")
  }

  private def events(): Unit = {
    sql.executeSQL(s"CREATE TABLE IF NOT EXISTS Events(EventName TEXT NOT NULL,counter INT, PRIMARY KEY(EventName(64)));")
  }

  private def storage(): Unit = {
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Storage(UUID TEXT,invData TEXT);")
  }

}

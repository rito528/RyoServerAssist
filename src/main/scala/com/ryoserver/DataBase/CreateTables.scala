package com.ryoserver.DataBase

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
    sql.createTable("StackData",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("UUID","TEXT",null),
      ColumnData("category","TEXT",null),
      ColumnData("item","TEXT",null),
      ColumnData("amount","INT",null)
    ))
    sql.createTable("StackList",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("category","TEXT",null),
      ColumnData("page","INT",null),
      ColumnData("invItem","TEXT",null),
    ))
  }

  private def gachaItems(): Unit = {
    sql.createTable("GachaItems",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("Rarity","INT",null),
      ColumnData("Material","TEXT",null)
    ))
  }

  private def distribution(): Unit = {
    sql.createTable("Distribution",List(
      ColumnData("id","INT",null,isPrimaryKey = true),
      ColumnData("GachaPaperType","TEXT",null),
      ColumnData("Count","INT",null)
    ))
  }

  private def homes(): Unit = {
    sql.createTable("Homes",List(
      ColumnData("UUID","TEXT",null),
      ColumnData("point","INT",null),
      ColumnData("Location","TEXT",null),
      ColumnData("Locked","BOOLEAN",null)
    ))
  }

  private def players(): Unit = {
    println("players")
    sql.createTable("Players",List(
      ColumnData("UUID","TEXT",null),
      ColumnData("lastLogin","DATETIME",null),
      ColumnData("lastLogout","DATETIME",null),
      ColumnData("loginDays","INT",null),
      ColumnData("consecutiveLoginDays","INT",null),
      ColumnData("lastDistributionReceived","INT",null),
      ColumnData("EXP","DOUBLE",null),
      ColumnData("Level","INT",null),
      ColumnData("questClearTimes","INT",null),
      ColumnData("gachaTickets","INT",null),
      ColumnData("gachaPullNumber","INT",null),
      ColumnData("SkillPoint","Double",null),
      ColumnData("SkillOpenPoint","INT",null),
      ColumnData("OpenedSkills","TEXT",null),
      ColumnData("SpecialSkillOpenPoint","INT","DEFAULT 0"),
      ColumnData("OpenedSpecialSkills","TEXT",null),
      ColumnData("OpenedTitles","TEXT",null),
      ColumnData("SelectedTitle","TEXT",null),
      ColumnData("EventTitles","TEXT",null),
      ColumnData("autoStack","BOOLEAN",null),
      ColumnData("VoteNumber","INT",null),
      ColumnData("LastVote","DATETIME","DEFAULT \"2022-01-01 00:00:00\""),
      ColumnData("ContinueVoteNumber","INT","DEFAULT 0"),
      ColumnData("LastDailyQuest","DATETIME","DEFAULT \"2022-01-01 00:00:00\""),
      ColumnData("Twitter","TEXT",null),
      ColumnData("Discord","TEXT",null),
      ColumnData("Word","TEXT",null)
    ))
  }

  private def eventRankings(): Unit = {
    sql.createTable("EventRankings",List(
      ColumnData("UUID","TEXT",null),
      ColumnData("EventName","TEXT",null),
      ColumnData("counter","INT",null)
    ))
  }

  private def events(): Unit = {
    sql.createTable("Events",List(
      ColumnData("EventName","TEXT","NOT NULL",isPrimaryKey = true),
      ColumnData("counter","INT",null),
      ColumnData("GivenGachaTickets","INT","DEFAULT 0"),
    ))
  }

  private def storage(): Unit = {
    sql.createTable("Storage",List(
      ColumnData("UUID","TEXT",null),
      ColumnData("invData","TEXT",null)
    ))
  }

}

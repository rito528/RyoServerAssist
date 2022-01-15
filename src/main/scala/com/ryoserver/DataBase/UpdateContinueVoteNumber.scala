package com.ryoserver.DataBase

import com.ryoserver.util.SQL

class UpdateContinueVoteNumber {

  def update(): Unit = {
    val sql = new SQL
    sql.executeSQL("UPDATE Players SET ContinueVoteNumber = CASE WHEN DATEDIFF(LastVote, NOW()) <= -2 THEN 0 ELSE ContinueVoteNumber END")
    sql.close()
  }

}

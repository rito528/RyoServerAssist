package com.ryoserver.DataBase

import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class UpdateContinueVoteNumber {

  def update(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"UPDATE Players SET ContinueVoteNumber = CASE WHEN DATEDIFF(LastVote, NOW()) <= -2 THEN 0 ELSE ContinueVoteNumber END".execute.apply()
  }

}

package com.ryoserver.DataBase

import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class UpdateContinueVoteNumber {

  def update(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"UPDATE Players SET continue_vote_number = CASE WHEN DATEDIFF(last_vote, NOW()) <= -8 THEN 0 ELSE continue_vote_number END".execute.apply()
  }

}

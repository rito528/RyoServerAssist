package com.ryoserver.Player

import org.bukkit.entity.Player
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class UpdateData {

  def updateReLogin(p: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"""UPDATE Players SET loginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) <= -1 THEN loginDays + 1 ELSE loginDays
      END,
      consecutiveLoginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) = -1 THEN consecutiveLoginDays + 1
      WHEN DATEDIFF(lastLogin, NOW()) <> 0 AND DATEDIFF(lastLogin, NOW()) <= -1 THEN 0
      ELSE consecutiveLoginDays
      END,
      lastLogin = NOW()
      WHERE UUID=${p.getUniqueId.toString}""".execute.apply()
  }

}

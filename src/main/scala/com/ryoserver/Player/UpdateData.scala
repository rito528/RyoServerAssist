package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class UpdateData(ryoServerAssist: RyoServerAssist) {

  def update(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val query = "UPDATE Players SET loginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) <= -1 THEN loginDays + 1 ELSE loginDays " +
    "END," +
    "consecutiveLoginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) = -1 THEN consecutiveLoginDays + 1 " +
    "WHEN DATEDIFF(lastLogin, NOW()) <> 0 AND DATEDIFF(lastLogin, NOW()) <= -1 THEN 0 " +
    "ELSE consecutiveLoginDays " +
    "END," +
    "lastLogin = NOW() " +
    "WHERE UUID='e1ee55bb-c993-4896-88e9-9893a11df27a';"
    sql.executeSQL(query)
    sql.close()
  }

}

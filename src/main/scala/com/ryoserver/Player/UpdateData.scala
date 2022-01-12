package com.ryoserver.Player

import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class UpdateData {

  def update(p: Player): Unit = {
    val sql = new SQL()
    val query = "UPDATE Players SET loginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) <= -1 THEN loginDays + 1 ELSE loginDays " +
      "END," +
      "consecutiveLoginDays = CASE WHEN DATEDIFF(lastLogin, NOW()) = -1 THEN consecutiveLoginDays + 1 " +
      "WHEN DATEDIFF(lastLogin, NOW()) <> 0 AND DATEDIFF(lastLogin, NOW()) <= -1 THEN 0 " +
      "ELSE consecutiveLoginDays " +
      "END," +
      "lastLogin = NOW() " +
      s"WHERE UUID='${p.getUniqueId.toString}';"
    sql.executeSQL(query)
    sql.close()
  }

}

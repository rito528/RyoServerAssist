package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class updateLevel(ryoServerAssist: RyoServerAssist) {

  def updateExp(exp: Int,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val calLv = new CalLv(ryoServerAssist)
    val data = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var old_exp = 0
    var old_level = 0
    if (data.next()) {
      old_exp = data.getInt("EXP")
      old_level = data.getInt("Level")
    }
    val sumExp = exp + old_exp
    val ticketAmountCal = (sumExp / 100) - (old_exp / 100)
    if ((ticketAmountCal / 100) <= 1) {
      sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + $ticketAmountCal WHERE UUID='${p.getUniqueId.toString}';")
    }
    if (calLv.getLevel(exp) != old_level && calLv.getLevel(exp) > old_exp) {
      val ticketCal = (calLv.getLevel(exp) - old_level) * 32
      sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + $ticketCal WHERE UUID='${p.getUniqueId.toString}'")
    }
    sql.executeSQL(s"UPDATE Players SET EXP=$exp,Level=${calLv.getLevel(exp)} WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
    BossBar.updateLevelBer(ryoServerAssist,exp, p)
  }
}

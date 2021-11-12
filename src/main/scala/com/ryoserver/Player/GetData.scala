package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class GetData(ryoServerAssist: RyoServerAssist) {

  def getTickets(p: Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT gachaTickets FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    var tickets = 0
    if (rs.next()) tickets = rs.getInt("gachaTickets")
    sql.close()
    tickets
  }

  def getFromAdminTickets(p: Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT lastDistributionReceived FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    if (!rs.next()) {
      sql.close()
      return 0
    }
    val lastReceived = rs.getInt("lastDistributionReceived")
    val ticketAmountSQL = sql.executeQuery(s"SELECT SUM(Count) AS Amount FROM Distribution WHERE id > $lastReceived")
    if (!ticketAmountSQL.next()) {
      sql.close()
      return 0
    }
    val ticketAmount = ticketAmountSQL.getInt("Amount")
    sql.close()
    ticketAmount
  }

}

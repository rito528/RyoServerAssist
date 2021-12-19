package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class RyoServerPlayer(player:Player,ryoServerAssist: RyoServerAssist) {

  val uuid: String = player.getUniqueId.toString

  def giveNormalGachaTicket(amount:Int): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + $amount WHERE UUID='$uuid'")
    sql.close()
  }

}

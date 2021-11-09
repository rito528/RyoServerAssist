package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.CreateData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class GetPlayerData(ryoServerAssist: RyoServerAssist) {

  def getPlayerExp(p: Player): Double = {
    new CreateData(ryoServerAssist).createPlayerData(p)
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) return rs.getDouble("EXP")
    sql.close()
    0
  }

  def getPlayerLevel(p: Player): Int = new CalLv(ryoServerAssist).getLevel(getPlayerExp(p).toInt)

}

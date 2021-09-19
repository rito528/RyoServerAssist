package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.createData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class getPlayerData(ryoServerAssist: RyoServerAssist) {

  def getPlayerExp(p: Player):Int = {
    new createData(ryoServerAssist).createPlayerData(p)
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) return rs.getInt("EXP")
    sql.close()
    0
  }

  def getPlayerLevel(p: Player):Int = new CalLv(ryoServerAssist).getLevel(getPlayerExp(p))

}

package com.ryoserver.Level.Player

import com.ryoserver.Player.createData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class LevelLoader(ryoServerAssist: RyoServerAssist) {

  def loadPlayerLevel(p:Player): Unit = {
    new createData(ryoServerAssist).createPlayerData(p)
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) {
      val exp = rs.getInt("EXP")
      BossBar.createLevelBer(ryoServerAssist,exp, p)
    }
    sql.close()
  }

}

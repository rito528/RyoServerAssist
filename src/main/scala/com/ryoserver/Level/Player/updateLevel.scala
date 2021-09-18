package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player

class updateLevel(ryoServerAssist: RyoServerAssist) {

  def updateExp(exp: Int,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET EXP=$exp,Level=${new CalLv(ryoServerAssist).getLevel(exp)}")
    sql.close()
    BossBar.updateLevelBer(ryoServerAssist,exp, p)
  }
}

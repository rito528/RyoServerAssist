package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.createData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

class LevelLoader(ryoServerAssist: RyoServerAssist) {

  def loadPlayerLevel(p:Player): Unit = {
    new createData(ryoServerAssist).createPlayerData(p)
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    if (rs.next()) {
      val exp = rs.getInt("EXP")
      val calLv = new CalLv(ryoServerAssist)
      val bossBer = Bukkit.createBossBar("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp, BarColor.GREEN, BarStyle.SOLID)
      val lv = calLv.getLevel(exp)
      bossBer.setProgress(1.0 - ((calLv.getSumTotal(lv + 1) - exp.toDouble) / calLv.getExp(lv + 1)))
      bossBer.setTitle("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp + " 次のレベルまで残り:" + (calLv.getSumTotal(lv + 1) - exp))
      bossBer.setVisible(true)
      bossBer.addPlayer(p)
    }
    sql.close()
  }

}

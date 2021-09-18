package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

object BossBar {

  def createLevelBer(ryoServerAssist: RyoServerAssist,exp:Int,p:Player): Unit = {
    val calLv = new CalLv(ryoServerAssist)
    val bossBer = Bukkit.createBossBar("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp, BarColor.GREEN, BarStyle.SOLID)
    val lv = calLv.getLevel(exp)
    if (calLv.MAX_LV > lv) {
      bossBer.setTitle("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp + " 次のレベルまで残り:" + (calLv.getSumTotal(lv + 1) - exp))
      bossBer.setProgress(1.0 - ((calLv.getSumTotal(lv + 1) - exp.toDouble) / calLv.getExp(lv + 1)))
    } else {
      bossBer.setProgress(1)
    }
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
  }

}

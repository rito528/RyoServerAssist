package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object BossBar {

  private var bers:mutable.Map[Player,org.bukkit.boss.BossBar] = mutable.Map.empty

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
    bers = bers + (p -> bossBer)
  }

  def updateLevelBer(ryoServerAssist:RyoServerAssist,exp: Int,p:Player): Unit = {
    val calLv = new CalLv(ryoServerAssist)
    val lv = calLv.getLevel(exp)
    val bossBer = bers.get(p)
    if (calLv.MAX_LV > lv) {
      bossBer.get.setTitle("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp + " 次のレベルまで残り:" + (calLv.getSumTotal(lv + 1) - exp))
      bossBer.get.setProgress(1.0 - ((calLv.getSumTotal(lv + 1) - exp.toDouble) / calLv.getExp(lv + 1)))
    } else {
      bossBer.get.setTitle("Lv." + calLv.getLevel(exp) + " 総EXP:" + exp)
      bossBer.get.setProgress(1)
    }
  }

}

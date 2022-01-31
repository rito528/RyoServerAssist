package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.util.Format
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object BossBar {

  private var bers: mutable.Map[Player, org.bukkit.boss.BossBar] = mutable.Map.empty

  def createLevelBer(exp: Double, p: Player): Unit = {
    val calLv = new CalLv
    val formattedExp = Format.threeCommaFormat(exp)
    val bossBer = Bukkit.createBossBar("Lv." + calLv.getLevel(exp.toInt) + " 総EXP:" + formattedExp, BarColor.GREEN, BarStyle.SOLID)
    val lv = calLv.getLevel(exp.toInt)
    val remainingExp = calLv.getSumTotal(lv + 1) - exp
    val formattedRemainingExp = Format.threeCommaFormat(remainingExp)
    if (calLv.MAX_LV > lv) {
      bossBer.setTitle("Lv." + calLv.getLevel(exp.toInt) + " 総EXP:" + formattedExp + " 次のレベルまで残り: " + formattedRemainingExp)
      bossBer.setProgress(1.0 - ((calLv.getSumTotal(lv + 1) - exp) / calLv.getExp(lv + 1)))
    } else {
      bossBer.setProgress(1)
    }
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
    bers += (p -> bossBer)
  }

  def updateLevelBer(exp: Double, p: Player): Unit = {
    val calLv = new CalLv
    val lv = calLv.getLevel(exp.toInt)
    val bossBer = bers.get(p)
    val formattedExp = Format.threeCommaFormat(exp)
    val remainingExp = calLv.getSumTotal(lv + 1) - exp
    val formattedRemainingExp = Format.threeCommaFormat(remainingExp)
    if (calLv.MAX_LV > lv) {
      bossBer.get.setTitle("Lv." + calLv.getLevel(exp.toInt) + " 総EXP:" + formattedExp + " 次のレベルまで残り: " + formattedRemainingExp)
      bossBer.get.setProgress(1.0 - ((calLv.getSumTotal(lv + 1) - exp) / calLv.getExp(lv + 1)))
    } else {
      bossBer.get.setTitle("Lv." + calLv.getLevel(exp.toInt) + " 総EXP:" + formattedExp)
      bossBer.get.setProgress(1)
    }
  }

  def unloadLevelBer(p: Player): Unit = {
    val ber = bers(p)
    ber.setVisible(false)
    bers = bers.filterNot { case (player, _) => p == player }
  }

}

package com.ryoserver.Level

import com.ryoserver.Config.ConfigData

class CalLv {

  val MAX_LV: Int = ConfigData.getConfig.maxLv

  /*
    経験値から現在のレベルを算出する
   */
  def getLevel(exp: Double, limit: Boolean = true): Int = {
    var lv = 0
    //順番にレベルそのレベルに到達するのに必要なexpを取得
    while (getSumTotal(lv + 1) <= exp && (!limit || lv < MAX_LV)) lv += 1
    lv
  }

  /*
    レベルを指定して次のレベルまでのExpを取得
   */
  def getExp(level: Int): Int = {
    var exp = 0.0
    if (level <= 100) exp = 10 * Math.pow(1.07, level - 1)
    else {
      val minusExp = getExp(level - 1)
      exp = minusExp + (minusExp / 100)
    }
    exp.toInt
  }

  /*
    指定したレベルになるために必要なEXPの総量を取得
   */
  def getSumTotal(level: Int): Int = {
    var sum = 0
    for (i <- 1 to level) sum += getExp(i)
    sum
  }
}

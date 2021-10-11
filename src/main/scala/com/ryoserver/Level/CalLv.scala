package com.ryoserver.Level

import com.ryoserver.RyoServerAssist

class CalLv(ryoServerAssist: RyoServerAssist) {
  /*
    レベルの計算をするクラス
   */

  val MAX_LV: Int = ryoServerAssist.getConfig.getInt("maxLv")

  def getLevel(exp: Int,limit: Boolean = true): Int = {
    /*
      経験値から現在のレベルを算出する
     */
    var lv = 0
    //順番にレベルそのレベルに到達するのに必要なexpを取得
    while (getSumTotal(lv + 1) <= exp && (!limit || lv < MAX_LV))lv += 1
    lv
  }

  def getExp(level: Int): Int = {
    /*
      レベルを指定して次のレベルまでのExpを取得
     */
    var exp = 0.0
    if (level <= 100) exp = 10 * Math.pow(1.07,level - 1)
    else exp = getExp(100) + (level * 100)
    exp.toInt
  }

  def getSumTotal(level: Int): Int = {
    /*
      指定したレベルになるために必要なEXPの総量を取得
     */
    var sum = 0
    for (i <- 1 to level) sum += getExp(i)
    sum
  }
}

package com.ryoserver.util

object Format {

  /*
    浮動小数点数を3桁区切り且つ小数点第2位以下を四捨五入して取得する
   */
  def threeCommaFormat(num: Double): String = {
    f"${num.toInt}%,3d" + f"${num - num.toInt}%.1f".replace("0.",".")
  }

}

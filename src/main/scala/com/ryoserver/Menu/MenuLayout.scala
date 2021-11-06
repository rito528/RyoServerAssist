package com.ryoserver.Menu

object MenuLayout {

  def getLayOut(x: Int, y: Int): Int = (((y - 1) * 9) + x) - 1

  def getSlot(y: Int): Int = y * 9

}

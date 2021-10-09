package com.ryoserver.Title

import com.ryoserver.RyoServerAssist

object TitleData {

  var lv: Array[String] = Array.empty
  var continuousLogin: Array[String] = Array.empty
  var loginDays: Array[String] = Array.empty
  var questClearNumber: Array[String] = Array.empty
  var gachaNumber: Array[String] = Array.empty
  var skillOpen: Array[String] = Array.empty
  var loginYear: Array[String] = Array.empty
  var loginPeriod: Array[String] = Array.empty
  var loginDay: Array[String] = Array.empty
  var titleGetNumber: Array[String] = Array.empty

  def isEnableTitle(ryoServerAssist: RyoServerAssist,title:String): Boolean = ryoServerAssist.getConfig.getStringList("enableTitles").contains(title)

}

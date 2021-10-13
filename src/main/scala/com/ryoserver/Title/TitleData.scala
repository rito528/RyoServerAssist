package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import org.bukkit.configuration.file.YamlConfiguration

import java.nio.file.Paths

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
  var continuousLoginAndQuestClearNumber: Array[String] = Array.empty

  def isEnableTitle(ryoServerAssist: RyoServerAssist,title:String): Boolean = {
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    titleConfig.getConfigurationSection("titles").getKeys(false).contains(title)
  }

}

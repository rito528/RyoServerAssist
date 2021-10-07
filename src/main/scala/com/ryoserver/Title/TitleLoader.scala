package com.ryoserver.Title

import com.ryoserver.RyoServerAssist
import org.bukkit.configuration.file.YamlConfiguration

import java.nio.file.Paths

class TitleLoader() {

  def loadTitle(): Unit = {
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    titleConfig.getConfigurationSection("titles").getKeys(false).forEach(title => {
      titleConfig.getString(s"titles.$title.type").toLowerCase() match {
        case "lv" => TitleData.lv :+= title
        case "continuouslogin" => TitleData.continuousLogin :+= title
        case "logindays" => TitleData.loginDays :+= title
        case "questclearnumber" => TitleData.questClearNumber :+= title
        case "gachanumber" => TitleData.gachaNumber :+= title
        case "skillopen" => TitleData.skillOpen :+= title
        case "loginyear" => TitleData.loginYear :+= title
        case "loginperiod" => TitleData.loginPeriod :+= title
        case "titlegetnumber" => TitleData.titleGetNumber :+= title
      }
    })
  }

}

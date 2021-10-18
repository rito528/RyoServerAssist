package com.ryoserver.Security

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

object Config {

  var config: FileConfiguration = null

  def getConfigOperators: Array[AnyRef] = {
    config.getStringList("authority").toArray()
  }

  def isExclusionUsers(p: Player): Boolean = {
    config.getStringList("exclusion").toArray().contains(p.getName)
  }

  def getBanCommands(cmd: String): Boolean = {
    config.getStringList("enableCommands").toArray().contains(cmd)
  }

  def getDoNotProtectionWorld(worldName: String): Boolean = {
    config.getStringList("worldDoNotProtection").toArray().contains(worldName)
  }

  def getApiKey: String = {
    config.getString("IPHunterAPIKey")
  }

}

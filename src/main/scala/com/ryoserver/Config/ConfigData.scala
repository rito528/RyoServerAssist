package com.ryoserver.Config

import com.ryoserver.RyoServerAssist

import scala.jdk.CollectionConverters._

object ConfigData {

  /*
    configのデータを格納・編集するためのオブジェクト
    configデータのアクセス制限のためにgetter/setter(loadConfig)を用意し、使用する
   */

  private var configData: ConfigDataType = _

  def getConfig: ConfigDataType = configData

  def loadConfig(ryoServerAssist: RyoServerAssist): Unit = {
    val config = ryoServerAssist.getConfig
    configData = ConfigDataType(
      host = config.getString("host"),
      user = config.getString("user"),
      pw = config.getString("pw"),
      db = config.getString("db"),
      log = config.getBoolean("log"),
      per = config.getDouble("per"),
      bigPer = config.getDouble("bigPer"),
      Special = config.getDouble("Special"),
      maxLv = config.getInt("maxLv"),
      tipsTimer = config.getInt("tipsTimer"),
      protectionWorlds = config.getStringList("protectionWorlds").asScala.toList,
      regenerationNormalWorlds = config.getStringList("regenerationNormalWorlds").asScala.toList,
      regenerationNetherWorlds = config.getStringList("regenerationNetherWorlds").asScala.toList,
      regenerationEndWorlds = config.getStringList("regenerationEndWorlds").asScala.toList,
      gachaChangeRate = config.getInt("gachaChangeRete"),
      webSite = config.getString("webSite"),
      dynmap = config.getString("dynmap"),
      JapanMinecraftServers = config.getString("JapanMinecraftServers"),
      monocraft = config.getString("monocraft"),
      IPHunterAPIKey = config.getString("IPHunterAPIKey"),
      exclusion = config.getStringList("exclusion").asScala.toList,
      authority = config.getStringList("authority").asScala.toList,
      ipInfo = config.getBoolean("ipInfo"),
      enableCommands = config.getStringList("enableCommands").asScala.toList,
      worldDoNotProtection = config.getStringList("worldDoNotProtection").asScala.toList,
      autoWorldRegeneration = config.getBoolean("autoWorldRegeneration"),
      notSpecialSkillWorlds = config.getStringList("notSpecialSkillWorlds").asScala.toList
    )
  }

}

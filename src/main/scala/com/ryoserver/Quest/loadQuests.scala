package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
class loadQuests(ryoServerAssist: RyoServerAssist) {

  private val QUEST_SETTING_FILE = "plugins/RyoServerAssist/Quests.yml"

  def createSetQuestFile(): Unit = {
    val ymlFile = Paths.get(QUEST_SETTING_FILE)
    if (Files.notExists(ymlFile)){
      ymlFile.toFile.createNewFile()
      val pw = new PrintWriter(ymlFile.toFile.getPath)
      pw.println("#クエスト設定用ファイルです。")
      pw.println("#設定方法はRedMine(http://192.168.0.51/issues/83)を参照してください。")
      pw.println("#また、クエストを有効化するにはconfig.ymlのenableQuestsにクエスト名を記載してください。")
      pw.close()
    }
  }

  def checkQuest(): Unit = {
    val quests = ryoServerAssist.getConfig.getStringList("enableQuests")
    val questConfig:FileConfiguration = YamlConfiguration.loadConfiguration(Paths.get(QUEST_SETTING_FILE).toFile)
    quests.forEach(questName => {
      if (!questConfig.contains(questName)) ryoServerAssist.getLogger.warning(questName + "という不明なクエストが指定されています！")
    })
  }

}

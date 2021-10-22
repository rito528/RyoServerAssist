package com.ryoserver.Quest

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.ryoserver.RyoServerAssist
import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import scala.io.Source
object loadQuests{

  val QUEST_SETTING_FILES = "plugins/RyoServerAssist/Quests/"

  var enableEvents: Array[String] = Array.empty
  var questConfig:FileConfiguration = _
  var langFile:JsonNode = _

  def checkQuest(): Unit = {
    if (new File(QUEST_SETTING_FILES).listFiles() == null) return
    new File(QUEST_SETTING_FILES).listFiles().foreach(file => {
      println("クエストロード中:" + file.getName)
      if (file.getName.contains(".json")) enableEvents +:= file.getName.replace(".json","")
    })
    val is = getClass.getClassLoader.getResourceAsStream("ja_jp.json")
    val mapper = new ObjectMapper()
    langFile = mapper.readTree(Source.fromInputStream(is).getLines().mkString)
  }

}

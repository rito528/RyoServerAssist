package com.ryoserver.Quest

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.ryoserver.RyoServerAssist
import org.bukkit.Material
import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}
import org.bukkit.entity.EntityType

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import java.util.stream.{Collectors, StreamSupport}
import scala.io.Source
object loadQuests {

  val QUEST_SETTING_FILES = "plugins/RyoServerAssist/Quests/"

  var enableEvents: Array[String] = Array.empty
  var questConfig:FileConfiguration = _
  var langFile:JsonNode = _

  def checkQuest(ryoServerAssist: RyoServerAssist): Unit = {
    if (new File(QUEST_SETTING_FILES).listFiles() == null) return
    new File(QUEST_SETTING_FILES).listFiles().foreach(file => {
      println("クエストロード中:" + file.getName)
      if (file.getName.contains(".json")) {
        val mapper = new ObjectMapper()
        var readLine = ""
        val questName = file.getName.replace(".json", "")
        enableEvents +:= questName
        val source = Source.fromFile(QUEST_SETTING_FILES + "/" + questName + ".json", "UTF-8")
        source.getLines().foreach(line => readLine = line)
        val json = mapper.readTree(readLine)
        val items = StreamSupport.stream(json.get("condition").spliterator(), false)
          .map(
            e => {
              e.asText
            })
          .collect(Collectors.toList[String])
        items.forEach(material => {
          if (Material.getMaterial(material.toUpperCase().split(":")(0)) == null && json.get("type").textValue() == "delivery")
            ryoServerAssist.getLogger.warning("クエスト:" + questName + "でアイテム名:" + material + "というアイテムが存在しません！")
          else if (!checkEntity(material.toUpperCase().split(":")(0)) && json.get("type").textValue() == "suppression")
            ryoServerAssist.getLogger.warning("クエスト:" + questName + "でアイテム名:" + material + "というMOBが存在しません！")
        })
      }
    })
    val is = getClass.getClassLoader.getResourceAsStream("ja_jp.json")
    val mapper = new ObjectMapper()
    langFile = mapper.readTree(Source.fromInputStream(is).getLines().mkString)
  }

  def checkEntity(entityName:String): Boolean = {
    val entities: Seq[String] = for {
      entity <- EntityType.values()
      if (entity.name() == entityName)
    } yield entity.name()

    entities.contains(entityName)
  }

}

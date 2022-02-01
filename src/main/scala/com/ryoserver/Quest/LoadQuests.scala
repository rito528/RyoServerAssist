package com.ryoserver.Quest

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity
import com.ryoserver.util.Logger.getLogger
import org.bukkit.Material

import java.io.File
import java.util.stream.{Collectors, StreamSupport}
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

object LoadQuests {

  val QUEST_SETTING_FILES = "plugins/RyoServerAssist/Quests/"
  val DAILYQUEST_SETTING_FILES = "plugins/RyoServerAssist/DailyQuests/"

  var loadedQuests: List[QuestType] = List.empty
  var loadedDailyQuests: List[QuestType] = List.empty

  def loadQuest(): Unit = {
    if (new File(QUEST_SETTING_FILES).listFiles() != null) {
      val questFileNames = new File(QUEST_SETTING_FILES).listFiles()
        .map(_.getName)
        .filter(_.contains(".json"))
      loadedQuests = questFileNames.toList.map(questFileName => {
        val data = getQuestData(isDaily = false, questFileName)
        val requires = StreamSupport.stream(data.get("condition").spliterator(), false)
          .map(
            e => {
              e.asText
            })
          .collect(Collectors.toList[String]).asScala
        val questType = data.get("type").textValue()
        QuestType(data.get("questName").textValue(), questType, data.get("minLevel").textValue().toInt,
          data.get("maxLevel").textValue().toInt, data.get("exp").textValue().toDouble,
          requires.map(data =>
            data.split(":")(0) -> data.split(":")(1).toInt).toMap)
      })
    }
    if (new File(DAILYQUEST_SETTING_FILES).listFiles() != null) {
      val questFileNames = new File(DAILYQUEST_SETTING_FILES).listFiles()
        .map(_.getName)
        .filter(_.contains(".json"))
      loadedDailyQuests = questFileNames.toList.map(questFileName => {
        val data = getQuestData(isDaily = true, questFileName)
        val requires = StreamSupport.stream(data.get("condition").spliterator(), false)
          .map(
            e => {
              e.asText
            })
          .collect(Collectors.toList[String]).asScala
        val questType = data.get("type").textValue()
        QuestType(data.get("questName").textValue(), questType, data.get("minLevel").textValue().toInt,
          data.get("maxLevel").textValue().toInt, data.get("exp").textValue().toDouble,
          requires.map(data =>
            data.split(":")(0) -> data.split(":")(1).toInt).toMap)
      })
    }
  }

  private def getQuestData(isDaily: Boolean, questFileName: String): JsonNode = {
    val mapper = new ObjectMapper()
    var readLine = ""
    val source = Source.fromFile(s"${if (isDaily) DAILYQUEST_SETTING_FILES else QUEST_SETTING_FILES}/$questFileName", "UTF-8")
    source.getLines().foreach(line => readLine = line)
    val json = mapper.readTree(readLine)
    val questName = json.get("questName").textValue()
    val items = StreamSupport.stream(json.get("condition").spliterator(), false)
      .map(
        e => {
          e.asText
        })
      .collect(Collectors.toList[String])
    items.forEach(material => {
      if (Material.getMaterial(material.toUpperCase().split(":")(0)) == null && json.get("type").textValue() == "delivery") {
        getLogger.warning("クエスト:" + questName + "でアイテム名:" + material.toUpperCase().split(":")(0) + "というアイテムが存在しません！")
        return null
      } else if (!Entity.isExistsEntity(material.toUpperCase().split(":")(0)) && json.get("type").textValue() == "suppression") {
        getLogger.warning("クエスト:" + questName + "でMOB名:" + material.toUpperCase().split(":")(0) + "というMOBが存在しません！")
        return null
      }
    })
    json
  }

}

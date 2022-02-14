package com.ryoserver.Quest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryoserver.util.Entity
import org.bukkit.entity.EntityType
import org.bukkit.{Bukkit, Material}

import java.io.File
import java.util.stream.{Collectors, StreamSupport}
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

object QuestData {

  private def getQuestData(path: String): Set[QuestDataContext[_]] = {
   new File(path).listFiles()
      .map(_.getName)
      .filter(_.contains(".json"))
      .toList
      .map {fileName =>
        val objectMapper = new ObjectMapper()
        val source = Source.fromFile(s"$path$fileName","UTF-8")
        val jsonData = objectMapper.readTree(source.getLines().mkString(""))
        val questName = jsonData.get("questName").textValue()
        val questType = QuestType.values.filter(_.dataName == jsonData.get("type").textValue()).head
        val minLevel = jsonData.get("minLevel").intValue()
        val maxLevel = jsonData.get("maxLevel").intValue()
        val exp = jsonData.get("exp").doubleValue()
        val materialRequires: Map[Material,Int] = if (questType == QuestType.delivery) {
          StreamSupport.stream(jsonData.get("condition").spliterator(), false)
            .map(
              e => {
                e.asText
              })
            .collect(Collectors.toList[String]).asScala
            .map{data =>
              val splitData = data.split(":")
              if (questType == QuestType.delivery && Material.matchMaterial(splitData.head) == null) {
                println(splitData.head + "は存在しません!")
                Bukkit.shutdown()
              }
              Material.matchMaterial(splitData.head) -> splitData.last.toInt
            }.toMap
        } else {
          Map.empty
        }
        val suppressionRequires: Map[EntityType,Int] = if (questType == QuestType.suppression) {
          StreamSupport.stream(jsonData.get("condition").spliterator(), false)
            .map(
              e => {
                e.asText
              })
            .collect(Collectors.toList[String]).asScala
            .map{data =>
              val splitData = data.split(":")
              if (questType == QuestType.suppression && !Entity.isExistsEntity(splitData.head)) {
                println(splitData.head + "は存在しません!")
                Bukkit.shutdown()
              }
              Entity.getEntity(splitData.head) -> splitData.last.toInt
            }.toMap
        } else {
          Map.empty
        }
        if (questType == QuestType.delivery) {
          QuestDataContext(questName, questType, minLevel, maxLevel, exp, materialRequires).asInstanceOf[QuestDataContext[_]]
        } else {
          QuestDataContext(questName, questType, minLevel, maxLevel, exp, suppressionRequires).asInstanceOf[QuestDataContext[_]]
        }
      }.toSet
  }

  final val loadedQuestData: Set[QuestDataContext[_]] = getQuestData("plugins/RyoServerAssist/Quests/")

  final val loadedDailyQuestData: Set[QuestDataContext[_]] = getQuestData("plugins/RyoServerAssist/DailyQuests/")

}

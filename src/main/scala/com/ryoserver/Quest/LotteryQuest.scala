package com.ryoserver.Quest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryoserver.Quest.LoadQuests.QUEST_SETTING_FILES

import java.util.stream.{Collectors, StreamSupport}
import scala.io.Source

class LotteryQuest {

  var questType = ""
  var questName = ""
  var items: java.util.List[String] = _
  var mobs: java.util.List[String] = _
  var exp: Double = 0

  def canQuests(lv: Int): Array[String] = {
    var quests: Array[String] = Array.empty
    LoadQuests.enableEvents.foreach(quest => {
      val mapper = new ObjectMapper()
      var readLine = ""
      val source = Source.fromFile(QUEST_SETTING_FILES + "/" + quest + ".json", "UTF-8")
      source.getLines().foreach(line => readLine = line)
      source.close()
      val json = mapper.readTree(readLine)
      val min = json.get("minLevel").textValue().toInt
      val max = json.get("maxLevel").textValue().toInt
      if (lv >= min && lv <= max) quests :+= quest
    })
    quests
  }

  def loadQuestData(): Unit = {
    val mapper = new ObjectMapper()
    var readLine = ""
    val source = Source.fromFile(QUEST_SETTING_FILES + "/" + questName + ".json", "UTF-8")
    source.getLines().foreach(line => readLine = line)
    val json = mapper.readTree(readLine)
    questType = json.get("type").textValue()
    items = StreamSupport.stream(json.get("condition").spliterator(), false)
      .map(
        e => {
          e.asText
        })
      .collect(Collectors.toList[String])
    mobs = StreamSupport.stream(json.get("condition").spliterator(), false)
      .map(
        e => {
          e.asText
        })
      .collect(Collectors.toList[String])
    exp = json.get("exp").textValue().toDouble
  }


}

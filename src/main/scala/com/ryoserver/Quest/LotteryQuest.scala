package com.ryoserver.Quest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryoserver.Quest.LoadQuests.QUEST_SETTING_FILES

import java.security.SecureRandom
import java.util.stream.{Collectors, StreamSupport}
import scala.io.Source

class LotteryQuest {

  var questType = ""
  var questName = ""
  var items: java.util.List[String] = _
  var mobs: java.util.List[String] = _
  var exp: Int = 0


  def lottery(lv: Int): Unit = {
    val random = SecureRandom.getInstance("SHA1PRNG")
    var counter = 0
    var min = 0
    var max = 0
    do {
      counter += 1
      val r = random.nextInt(LoadQuests.enableEvents.length)
      questName = LoadQuests.enableEvents(r)
      val mapper = new ObjectMapper()
      var readLine = ""
      val source = Source.fromFile(QUEST_SETTING_FILES + "/" + questName + ".json", "UTF-8")
      source.getLines().foreach(line => readLine = line)
      source.close()
      val json = mapper.readTree(readLine)
      min = json.get("minLevel").textValue().toInt
      max = json.get("maxLevel").textValue().toInt
      if (lv >= min && lv <= max) loadQuestData()
    } while (lv < min || lv > max)
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
    exp = json.get("exp").textValue().toInt
  }


}

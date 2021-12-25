package com.ryoserver.Quest.Event

import com.fasterxml.jackson.databind.ObjectMapper

import java.io.File
import scala.io.Source

class EventLoader {

  val eventDataDir = "plugins/RyoServerAssist/Events"

  def loadEvent(): Unit = {
    val dir = new File(eventDataDir)
    if (!dir.exists()) dir.mkdirs()
    dir.listFiles
      .filter(_.getName.endsWith(".json"))
      .foreach(file => {
        var readLine = ""
        val source = Source.fromFile(file.getAbsolutePath, "UTF-8")
        source.getLines().foreach(line => readLine = line)
        source.close()
        val mapper = new ObjectMapper()
        val json = mapper.readTree(readLine)
        val eventName = json.get("eventName").textValue()
        val eventType = json.get("type").textValue()
        val start = json.get("start").textValue()
        val end = json.get("end").textValue()
        val exp = json.get("exp").textValue().toDouble
        if (eventType == "bonus") {
          EventDataProvider.eventData :+= EventType(eventName, eventType, start, end, null, exp, 0, 0)
        } else {
          val item = json.get("item").textValue()
          val reward = json.get("reward").textValue().toInt
          val distribution = json.get("distribution").textValue().toInt
          EventDataProvider.eventData :+= EventType(eventName, eventType, start, end, item, exp, reward, distribution)
        }
      })
  }

}

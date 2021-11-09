package com.ryoserver.Quest.Event

import scala.collection.mutable

object EventDataProvider {

  var eventData: Array[EventType] = Array.empty

  var eventCounter: Int = 0

  var eventRanking: mutable.Map[String, Int] = mutable.Map.empty //UUID,納品または討伐量

}

package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist

import java.security.SecureRandom

class LotteryQuest(ryoServerAssist: RyoServerAssist) {

  var questType: String = ""
  var questName:String = ""
  var items:java.util.List[String] = _
  var descriptions:java.util.List[String] = _
  var exp:Int = 0


  def lottery(lv: Int): Unit = {
    val random = SecureRandom.getInstance("SHA1PRNG")
    do {
      val r = random.nextInt(loadQuests.enableEvents.length)
      questName = loadQuests.enableEvents(r)
      if (lv >= loadQuests.questConfig.getInt(questName + ".minLevel")) getQuest()
    } while (lv < loadQuests.questConfig.getInt(questName + ".minLevel"))
  }

  def getQuest(): Unit = {
    questType = loadQuests.questConfig.getString(questName + ".type")
    items = loadQuests.questConfig.getStringList(questName + ".deliveryItems")
    descriptions = loadQuests.questConfig.getStringList(questName + ".description")
    exp = loadQuests.questConfig.getInt(questName + ".exp")
  }


}

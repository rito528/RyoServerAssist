package com.ryoserver.Quest

import java.security.SecureRandom

class LotteryQuest {

  var questType = ""
  var questName = ""
  var items:java.util.List[String] = _
  var mobs:java.util.List[String] = _
  var descriptions:java.util.List[String] = _
  var exp:Int = 0


  def lottery(lv: Int): Unit = {
    val random = SecureRandom.getInstance("SHA1PRNG")
    var counter = 0
    do {
      counter += 1
      val r = random.nextInt(loadQuests.enableEvents.length)
      questName = loadQuests.enableEvents(r)
      println(lv)
      println(loadQuests.questConfig.getInt(questName + ".maxLevel"))
      if (lv >= loadQuests.questConfig.getInt(questName + ".minLevel") && lv <= loadQuests.questConfig.getInt(questName + ".maxLevel")) loadQuestData()
    } while (lv < loadQuests.questConfig.getInt(questName + ".minLevel") || lv > loadQuests.questConfig.getInt(questName + ".maxLevel"))
  }

  def loadQuestData(): Unit = {
    questType = loadQuests.questConfig.getString(questName + ".type")
    items = loadQuests.questConfig.getStringList(questName + ".deliveryItems")
    mobs = loadQuests.questConfig.getStringList(questName + ".suppressionMobs")
    descriptions = loadQuests.questConfig.getStringList(questName + ".description")
    exp = loadQuests.questConfig.getInt(questName + ".exp")
  }


}

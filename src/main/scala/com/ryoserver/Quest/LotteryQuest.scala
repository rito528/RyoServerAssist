package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist

import java.security.SecureRandom

class LotteryQuest(ryoServerAssist: RyoServerAssist) {

  def lottery(): List[AnyRef] = {
    val random = SecureRandom.getInstance("SHA1PRNG")
    val r = random.nextInt(loadQuests.enableEvents.length)
    val questName = loadQuests.enableEvents(r)
    val questType = loadQuests.questConfig.getString(questName + ".type")
    if (questType == "delivery") {
      /*
        クエスト名
        クエストタイプ
        納品アイテム
        説明
       */
      return List(questName,questType,loadQuests.questConfig.getStringList("deliveryItems"),loadQuests.questConfig.getStringList("description"))
    }
    List()
  }

}

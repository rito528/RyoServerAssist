package com.ryoserver.Quest

import com.ryoserver.Quest.Event.{EventDataProvider, EventGateway}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.Entity.getEntity
import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}

class SuppressionEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onEntityDeath(e: EntityDeathEvent): Unit = {
    val entity = e.getEntityType
    e.getEntity.getKiller match {
      case p: Player =>
        val questData = new QuestData(ryoServerAssist)
        val lottery = new LotteryQuest()
        var pause = false
        val eventGateway = new EventGateway(ryoServerAssist)
        var event = true
        if (questData.getSelectedQuest(p) != null) {
          lottery.questName = questData.getSelectedQuest(p)
          lottery.loadQuestData()
          if (lottery.questType == "suppression") {
            val remaining = questData.getSelectedQuestRemaining(p)
            var data: Array[String] = Array.empty[String]
            remaining.split(";").foreach(questEntity => {
              data :+= questEntity
              if (getEntity(questEntity.split(":")(0)) == entity) {
                event = false
                data = data.filterNot(_ == questEntity)
                var amount = questEntity.split(":")(1).toInt - 1
                if (amount < 0) amount = 0
                else if (amount % 5 == 0) pause = true
                data :+= questEntity.split(":")(0) + ":" + amount
              }
            })
            var questDone = true
            data.foreach(d => if (d.split(":")(1).toInt != 0) questDone = false)
            if (questDone) {
              p.sendMessage(ChatColor.AQUA + "おめでとうございます！クエストが完了しました！")
              p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
              questData.questClear(p)
              new GiveTitle(ryoServerAssist).questClearNumber(p)
              new GiveTitle(ryoServerAssist).continuousLoginAndQuestClearNumber(p)
            } else {
              questData.setSelectedQuestItemRemaining(p, data.mkString(";"))
            }
            if (!questDone && pause) {
              p.sendMessage("[進行状況]")
              data.foreach(e => {
                p.sendMessage(LoadQuests.langFile.get("entity." + getEntity(e.split(":")(0)).getKey.toString.replace(":", ".")).textValue() + "->" + e.split(":")(1) + "体")
              })
            }
          }
        }
        if (eventGateway.eventInfo(eventGateway.holdingEvent()) != null && eventGateway.eventInfo(eventGateway.holdingEvent()).eventType == "suppression" && event) {
          val eventEntity = getEntity(eventGateway.eventInfo(eventGateway.holdingEvent()).item)
          if (entity == eventEntity) {
            EventDataProvider.eventCounter += 1
            var counter = 0
            if (EventDataProvider.eventRanking.contains(p.getUniqueId.toString)) {
              counter = EventDataProvider.eventRanking(p.getUniqueId.toString)
              EventDataProvider.eventRanking = EventDataProvider.eventRanking
                .filterNot { case (uuid, _) => uuid == p.getUniqueId.toString }
            }
            EventDataProvider.eventRanking += (p.getUniqueId.toString -> counter)
          }
        }
      case _ =>
    }
  }

}

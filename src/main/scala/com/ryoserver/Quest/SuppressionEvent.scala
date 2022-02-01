package com.ryoserver.Quest

import com.ryoserver.Quest.Event.{EventDataProvider, EventGateway}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}

class SuppressionEvent(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onEntityDeath(e: EntityDeathEvent): Unit = {
    val entity = e.getEntityType
    e.getEntity.getKiller match {
      case p: Player =>
        val questGateway = new QuestGateway()
        val eventGateway = new EventGateway(ryoServerAssist)
        var event = true //通常のクエストをこなした場合はイベントの討伐体数を無効化する
        questGateway.getSelectedQuest(p) match {
          case Some(selectedQuestData) =>
            var questProgress = questGateway.getQuestProgress(p)
            if (selectedQuestData.questType == "suppression" && questProgress.keys.map(getEntity).toArray.contains(entity)) {
              event = false
              val targetProgress = questProgress(entity.name().toUpperCase())
              if (targetProgress != 0) {
                questProgress += (entity.name().toUpperCase() -> (targetProgress - 1))
                questGateway.setQuestProgress(p, questProgress)
                if (questProgress.forall { case (_, amount) => amount == 0 }) {
                  p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
                  p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
                  questGateway.questClear(p)
                  new GiveTitle().questClearNumber(p)
                  new GiveTitle().continuousLoginAndQuestClearNumber(p)
                } else if ((targetProgress - 1) % 5 == 0) {
                  p.sendMessage(s"$WHITE[クエストの進行状況]")
                  questProgress.foreach { case (entity, amount) =>
                    p.sendMessage(s"${Translate.entityNameToJapanese(getEntity(entity))}:残り${amount}体")
                  }
                }
              }
            }
          case None =>
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
            EventDataProvider.eventRanking += (p.getUniqueId.toString -> (counter + 1))
          }
        }
      case _ =>
    }
  }

}

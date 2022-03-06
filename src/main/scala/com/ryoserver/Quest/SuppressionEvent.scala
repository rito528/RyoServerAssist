package com.ryoserver.Quest

import com.ryoserver.Quest.Event.{EventDataProvider, EventGateway}
import com.ryoserver.Quest.MaterialOrEntityType.entityType
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
//    val entity = e.getEntityType
//    e.getEntity.getKiller match {
//      case p: Player =>
//        val questGateway = new QuestGateway(p)
//        val eventGateway = new EventGateway
//        var event = true //通常のクエストをこなした場合はイベントの討伐体数を無効化する
//        questGateway.getSelectedQuest match {
//          case Some(_) =>
//            val selectedQuestData = questGateway.getSelectedQuestData
//            if (selectedQuestData.questType == QuestType.suppression && selectedQuestData.requireList.contains(entityType(entity))) {
//              event = false
//              val uuid = p.getUniqueId
//              val selectedQuestPlayerData = QuestPlayerData.getPlayerQuestContext(uuid)
//              val nowProcess = selectedQuestPlayerData.progress.get
//              if (nowProcess(entityType(entity)) != 0) {
//                QuestPlayerData.setQuestData(uuid,
//                  QuestPlayerData.getPlayerQuestContext(uuid)
//                    .setProgress(Option(QuestPlayerData.getPlayerQuestContext(uuid).progress.get
//                      ++ Map(entityType(entity) -> (nowProcess(entityType(entity)) - 1)))
//                    )
//                )
//                if (QuestPlayerData.getPlayerQuestContext(uuid).progress.get.forall{case (_,amount) => amount == 0}) {
//                  p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
//                  p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
//                  questGateway.questClear()
//                  new GiveTitle().questClearNumber(p)
//                  new GiveTitle().continuousLoginAndQuestClearNumber(p)
//                } else if (QuestPlayerData.getPlayerQuestContext(uuid).progress.get.exists{case (_,amount) => amount % 5 == 0}) {
//                  p.sendMessage(s"$WHITE[クエストの進行状況]")
//                  QuestPlayerData.getPlayerQuestContext(uuid).progress.get.foreach { case (entity, amount) =>
//                    p.sendMessage(s"${Translate.entityNameToJapanese(entity.entityType)}:残り${amount}体")
//                  }
//                }
//              }
//            }
//          case None =>
//        }
//        if (eventGateway.eventInfo(eventGateway.holdingEvent()) != null && eventGateway.eventInfo(eventGateway.holdingEvent()).eventType == "suppression" && event) {
//          val eventEntity = getEntity(eventGateway.eventInfo(eventGateway.holdingEvent()).item)
//          if (entity == eventEntity) {
//            EventDataProvider.eventCounter += 1
//            var counter = 0
//            if (EventDataProvider.eventRanking.contains(p.getUniqueId.toString)) {
//              counter = EventDataProvider.eventRanking(p.getUniqueId.toString)
//              EventDataProvider.eventRanking = EventDataProvider.eventRanking
//                .filterNot { case (uuid, _) => uuid == p.getUniqueId.toString }
//            }
//            EventDataProvider.eventRanking += (p.getUniqueId.toString -> (counter + 1))
//          }
//        }
//      case _ =>
//    }
  }

}

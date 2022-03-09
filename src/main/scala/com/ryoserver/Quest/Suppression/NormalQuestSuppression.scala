package com.ryoserver.Quest.Suppression

import com.ryoserver.Quest.MaterialOrEntityType.entityType
import com.ryoserver.Quest.QuestServices.NormalQuestService
import com.ryoserver.Quest.{QuestPlayerData, QuestType}
import com.ryoserver.Title.GiveTitle
import com.ryoserver.util.Translate
import org.bukkit.Sound
import org.bukkit.event.Listener
import org.bukkit.ChatColor._

object NormalQuestSuppression {

  val executor: Listener = ContextualEntityDeathEventExecutor.entityDeathEventExecutor(new EntityDeathEventContext {
    override def execute(rawEntityDeathEventContext: RawEntityDeathEventContext): Unit = {
      val service = new NormalQuestService(rawEntityDeathEventContext.killer)
      service.getSelectedQuest match {
        case Some(_) =>
          val selectedQuestData = service.getSelectedQuestData
          if (selectedQuestData.questType != QuestType.suppression) return
          val uuid = rawEntityDeathEventContext.killer.getUniqueId
          val entity = rawEntityDeathEventContext.killedEntity
          if (selectedQuestData.questType == QuestType.suppression && selectedQuestData.requireList.contains(entityType(entity))) {
            val selectedQuestPlayerData = new QuestPlayerData().getQuestData.getPlayerQuestContext(uuid)
            val nowProcess = selectedQuestPlayerData.progress.get
            if (nowProcess(entityType(entity)) != 0) {
              new QuestPlayerData().processQuestData.selectQuest(uuid,
                new QuestPlayerData().getQuestData.getPlayerQuestContext(uuid)
                  .changeProgress(entityType(entity), nowProcess(entityType(entity)) - 1)
              )
              val p = rawEntityDeathEventContext.killer
              if (new QuestPlayerData().getQuestData.getPlayerQuestDataContext(uuid).progress.get.forall{case (_,amount) => amount == 0}) {
                p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
                service.questClear(selectedQuestData.exp)
                service.questDestroy()
                new GiveTitle().questClearNumber(p)
                new GiveTitle().continuousLoginAndQuestClearNumber(p)
              } else if (new QuestPlayerData().getQuestData.getPlayerQuestDataContext(uuid).progress.get.exists{case (_,amount) => amount % 5 == 0}) {
                p.sendMessage(s"$WHITE[クエストの進行状況]")
                new QuestPlayerData().getQuestData.getPlayerQuestDataContext(uuid).progress.get.foreach { case (entity, amount) =>
                  p.sendMessage(s"${Translate.entityNameToJapanese(entity.entityType)}:残り${amount}体")
                }
              }
            }
          }

        case None =>
      }
    }
  })

}

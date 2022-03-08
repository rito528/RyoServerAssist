package com.ryoserver.Quest.Suppression

import com.ryoserver.Quest.QuestServices.NormalQuestService
import com.ryoserver.Quest.{QuestPlayerData, QuestType}
import org.bukkit.event.Listener

object NormalQuestSuppression {

  val executor: Listener = ContextualEntityDeathEventExecutor.entityDeathEventExecutor(new EntityDeathEventContext {
    override def execute(rawEntityDeathEventContext: RawEntityDeathEventContext): Unit = {
      val service = new NormalQuestService(rawEntityDeathEventContext.killer)
      service.getSelectedQuest match {
        case Some(questName) =>
          val selectedQuestData = service.getSelectedQuestData
          if (selectedQuestData.questType != QuestType.suppression) return
          val playerQuestDataContext = new QuestPlayerData().getQuestData.getPlayerQuestDataContext(rawEntityDeathEventContext.killer.getUniqueId)


        case None =>
      }
    }
  })

}

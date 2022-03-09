package com.ryoserver.Quest.Suppression

import com.ryoserver.Quest.Event.{EventDataProvider, EventGateway}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.event.Listener

class EventSuppression(implicit ryoServerAssist: RyoServerAssist) {

  val executor: Listener = ContextualEntityDeathEventExecutor.entityDeathEventExecutor(new EntityDeathEventContext {
    override def execute(rawEntityDeathEventContext: RawEntityDeathEventContext): Unit = {
      val eventGateway = new EventGateway()
      if (eventGateway.eventInfo(eventGateway.holdingEvent()) != null && eventGateway.eventInfo(eventGateway.holdingEvent()).eventType == "suppression") {
        val eventEntity = getEntity(eventGateway.eventInfo(eventGateway.holdingEvent()).item)
        if (rawEntityDeathEventContext.killedEntity == eventEntity) {
          EventDataProvider.eventCounter += 1
          var counter = 0
          val p = rawEntityDeathEventContext.killer
          if (EventDataProvider.eventRanking.contains(p.getUniqueId.toString)) {
            counter = EventDataProvider.eventRanking(p.getUniqueId.toString)
            EventDataProvider.eventRanking = EventDataProvider.eventRanking
              .filterNot { case (uuid, _) => uuid == p.getUniqueId.toString }
          }
          EventDataProvider.eventRanking += (p.getUniqueId.toString -> (counter + 1))
        }
      }
    }
  })

}

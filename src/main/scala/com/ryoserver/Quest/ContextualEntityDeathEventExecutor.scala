package com.ryoserver.Quest

import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}

object ContextualEntityDeathEventExecutor {

  def entityDeathEventExecutor(entityDeathEventContext: EntityDeathEventContext): Listener = new Listener {

    @EventHandler
    def onEntityDeathEvent(e: EntityDeathEvent): Unit = {
      entityDeathEventContext.execute(RawEntityDeathEventContext(e.getEntity,e.getEntity.getKiller))
    }

  }

}

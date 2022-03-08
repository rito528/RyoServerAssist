package com.ryoserver.Quest.Suppression

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}

object ContextualEntityDeathEventExecutor {

  def entityDeathEventExecutor(entityDeathEventContext: EntityDeathEventContext): Listener = new Listener {

    @EventHandler//IDEからonEntityDeathEventは使用されていない等のメッセージが出ますが、実際には使用されています。
    def onEntityDeathEvent(e: EntityDeathEvent): Unit = {
      e.getEntity.getKiller match {
        case p: Player =>
          entityDeathEventContext.execute(RawEntityDeathEventContext(e.getEntityType,p))
        case _ =>
      }
    }

  }

}

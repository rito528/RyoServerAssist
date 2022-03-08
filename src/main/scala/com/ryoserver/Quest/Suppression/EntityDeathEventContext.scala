package com.ryoserver.Quest.Suppression

import org.bukkit.entity.{LivingEntity, Player}

case class RawEntityDeathEventContext(killedEntity: LivingEntity, killer: Player)

trait EntityDeathEventContext {

  def execute(rawEntityDeathEventContext: RawEntityDeathEventContext): Unit

}

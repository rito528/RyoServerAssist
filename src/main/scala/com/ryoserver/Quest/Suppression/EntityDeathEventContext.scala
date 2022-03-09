package com.ryoserver.Quest.Suppression

import org.bukkit.entity.{EntityType, Player}

case class RawEntityDeathEventContext(killedEntity: EntityType, killer: Player)

trait EntityDeathEventContext {

  def execute(rawEntityDeathEventContext: RawEntityDeathEventContext): Unit

}

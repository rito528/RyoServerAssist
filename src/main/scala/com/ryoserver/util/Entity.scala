package com.ryoserver.util

import org.bukkit.entity.EntityType

object Entity {

  def getEntity(name: String): EntityType = {
    EntityType.values().foreach(entity => if (entity.name().equalsIgnoreCase(name)) return entity)
    null
  }

}

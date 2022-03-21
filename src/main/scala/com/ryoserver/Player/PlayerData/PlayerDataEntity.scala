package com.ryoserver.Player.PlayerData

import com.ryoserver.Player.PlayerDataType

import java.util.UUID
import scala.collection.mutable

object PlayerDataEntity {

  private[PlayerData] val playerData: mutable.Map[UUID,PlayerDataType] = mutable.Map.empty

}

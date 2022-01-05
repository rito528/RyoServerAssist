package com.ryoserver.Player

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: mutable.Map[UUID, PlayerDataType] = mutable.Map.empty

}

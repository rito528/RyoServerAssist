package com.ryoserver.Player

import java.util.UUID
import scala.collection.mutable

object Data {

  var playerData: mutable.Map[UUID, PlayerData] = mutable.Map.empty

}

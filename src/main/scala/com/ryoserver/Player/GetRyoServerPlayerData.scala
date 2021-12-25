package com.ryoserver.Player

import org.bukkit.entity.Player

class GetRyoServerPlayerData(player: Player) {

  private val uuid = player.getUniqueId

  def getRanking: Int = Data.playerData.values.toSeq.sortBy(_.exp).reverse.indexOf(Data.playerData(uuid)) + 1

}

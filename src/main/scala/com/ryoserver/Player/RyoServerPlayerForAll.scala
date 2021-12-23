package com.ryoserver.Player

import org.bukkit.Bukkit

import java.util.UUID
import scala.collection.mutable

class RyoServerPlayerForAll {

  private val players: Iterable[String] = Data.playerData.keys
  Data.playerData = mutable.Map.empty

  def giveNormalGachaTickets(amount: Int): Unit = {
    players.foreach(uuid => {
      val offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid))
      new RyoServerPlayer(offlinePlayer).giveNormalGachaTicket(amount)
    })
  }

}

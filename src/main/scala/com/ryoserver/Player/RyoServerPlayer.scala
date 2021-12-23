package com.ryoserver.Player

import org.bukkit.OfflinePlayer

class RyoServerPlayer(player:OfflinePlayer) {

  private val uuid: String = player.getUniqueId.toString
  private val oldData: PlayerData = Data.playerData(uuid)
  Data.playerData = Data.playerData.filterNot{case (uuid,_) => uuid == this.uuid}

  def giveNormalGachaTicket(amount:Int): Unit = {
    Data.playerData += (uuid -> oldData.copy(gachaTickets = oldData.gachaTickets + amount))
  }

  def reduceNormalGachaTicket(amount:Int): Unit = {
    Data.playerData += (uuid -> oldData.copy(gachaTickets = oldData.gachaTickets - amount))
  }

  def addOneVoteNumber(): Unit = {
    Data.playerData += (uuid -> oldData.copy(voteNumber = oldData.voteNumber + 1))
  }

  def addGachaPullNumber(amount:Int): Unit = {
    Data.playerData += (uuid -> oldData.copy(gachaPullNumber = oldData.gachaPullNumber + amount))
  }



}

package com.ryoserver.Player

import com.ryoserver.Distribution.DistributionData
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player

class GetData {

  def getFromAdminTickets(p: Player): Int = {
    val lastReceived = p.getLastDistributionReceived
    var amount = 0
    DistributionData.distributionData.foreach(data => {
      if (data.id > lastReceived) {
        amount += data.amount
      }
    })
    amount
  }

}

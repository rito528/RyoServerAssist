package com.ryoserver.Player

import com.ryoserver.Distribution.DistributionData
import org.bukkit.entity.Player

class GetData {

  def getFromAdminTickets(p: Player): Int = {
    val lastReceived = Data.playerData(p.getUniqueId).lastDistributionReceived
    var amount = 0
    DistributionData.distributionData.foreach(data => {
      if (data.id > lastReceived) {
        amount += data.amount
      }
    })
    amount
  }

}

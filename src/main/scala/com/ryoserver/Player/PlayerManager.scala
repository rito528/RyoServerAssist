package com.ryoserver.Player

import com.ryoserver.Player.PlayerData.PlayerDataRepository
import org.bukkit.OfflinePlayer

object PlayerManager {

  implicit class getPlayerData(p: OfflinePlayer) {

    def getRyoServerData: PlayerDataType = {
      val uuid = p.getUniqueId
      new PlayerDataRepository().findBy(uuid).get
    }

  }

}

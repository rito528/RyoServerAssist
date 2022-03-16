package com.ryoserver.Player

import com.ryoserver.Player.PlayerData.PlayerDataRepository
import org.bukkit.OfflinePlayer

object PlayerManager {

  implicit class getPlayerData(p: OfflinePlayer) {

    def getRyoServerData: PlayerDataType = new PlayerDataRepository().findBy(p.getUniqueId).get

  }

}

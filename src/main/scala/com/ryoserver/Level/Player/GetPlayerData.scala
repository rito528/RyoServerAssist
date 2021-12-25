package com.ryoserver.Level.Player

import com.ryoserver.Player.Data
import org.bukkit.entity.Player

class GetPlayerData {

  def getPlayerExp(p: Player): Double = Data.playerData(p.getUniqueId).exp

  def getPlayerLevel(p: Player): Int = Data.playerData(p.getUniqueId).level

}

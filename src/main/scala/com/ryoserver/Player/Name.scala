package com.ryoserver.Player

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player

class Name(ryoServerAssist: RyoServerAssist) {

  def updateName(p:Player): Unit = {
    val name = s"[Lv.${new getPlayerData(ryoServerAssist).getPlayerLevel(p)}]${p.getName}"
    p.setDisplayName(name)
    p.setPlayerListName(name)
  }

}

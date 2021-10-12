package com.ryoserver.Player

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.PlayerTitleData
import org.bukkit.entity.Player

class Name(ryoServerAssist: RyoServerAssist) {

  def updateName(p:Player): Unit = {
    var title = ""
    val selectedTitle = new PlayerTitleData(ryoServerAssist).getSelectedTitle(p.getUniqueId.toString)
    if (selectedTitle != null) title = "[" + selectedTitle + "]"
    val name = s"$title[Lv.${new getPlayerData(ryoServerAssist).getPlayerLevel(p)}]${p.getName}"
    p.setDisplayName(name)
    p.setPlayerListName(name)
  }

}

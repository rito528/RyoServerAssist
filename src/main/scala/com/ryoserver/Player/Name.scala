package com.ryoserver.Player

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.PlayerTitleData
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class Name(ryoServerAssist: RyoServerAssist) {

  def updateName(p: Player): Unit = {
    var title = ""
    val selectedTitle = new PlayerTitleData(ryoServerAssist).getSelectedTitle(p.getUniqueId.toString)
    if (selectedTitle != null) title = "[" + selectedTitle + "]"
    val prefix = if (p.hasPermission("minecraft.command.gamemode")) ChatColor.LIGHT_PURPLE else ""
    val name = s"$title[Lv.${new GetPlayerData().getPlayerLevel(p)}]$prefix${p.getName}" + ChatColor.RESET
    p.setDisplayName(name)
    p.setPlayerListName(name)
  }

}

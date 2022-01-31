package com.ryoserver.Player

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Title.PlayerTitleData
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class Name {

  def updateName(p: Player): Unit = {
    var title = ""
    val selectedTitle = new PlayerTitleData().getSelectedTitle(p.getUniqueId)
    if (selectedTitle != null) title = "[" + selectedTitle + "]"
    val prefix = if (p.hasPermission("minecraft.command.gamemode")) ChatColor.LIGHT_PURPLE else ""
    val name = s"$title[Lv.${p.getQuestLevel}]$prefix${p.getName}" + ChatColor.RESET
    p.setDisplayName(name)
    p.setPlayerListName(name)
  }

}

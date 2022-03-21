package com.ryoserver.Title

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, OfflinePlayer}

import java.util.UUID

class PlayerTitleData {

  def openTitle(p: Player, title: String): Boolean = {
    val uuid = p.getUniqueId
    if (hasTitle(uuid, title)) return false
    implicit val player: Player = p
    player.getRyoServerData.addOpenedTitles(title).apply
    new GiveTitle().titleGetNumber(p)
    true
  }

  def hasTitle(uuid: UUID, title: String): Boolean = getHasTitles(uuid).contains(title)

  def getHasTitles(uuid: UUID): Set[String] = {
    Bukkit.getOfflinePlayer(uuid).getRyoServerData.openedTitles.getOrElse(Set.empty)
  }

  def removeTitle(uuid: UUID, title: String): Boolean = {
    if (!hasTitle(uuid, title)) return false
    implicit val p: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    p.getRyoServerData.removeOpenedTitles(title).apply
    true
  }

  def getSelectedTitle(uuid: UUID): String = {
    implicit val p: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    p.getRyoServerData.selectedTitle match {
      case Some(title) => title
      case None => null
    }
  }

  def setSelectTitle(uuid: UUID, title: String): Unit = {
    implicit val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    offlinePlayer.getRyoServerData.setSelectedTitle(Option(title))
  }

  def resetSelectTitle(uuid: UUID): Unit = {
    implicit val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    offlinePlayer.getRyoServerData.setSelectedTitle(None).apply
  }

}

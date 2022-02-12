package com.ryoserver.Title

import com.ryoserver.Player.PlayerData
import com.ryoserver.Player.PlayerManager.setPlayerData
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

class PlayerTitleData {

  def openTitle(p: Player, title: String): Boolean = {
    val uuid = p.getUniqueId
    if (hasTitle(uuid, title)) return false
    p.openTitles(getHasTitles(uuid).mkString(";") + (if (getHasTitles(uuid).mkString(";") != "") ";" else "") + title)
    new GiveTitle().titleGetNumber(p)
    true
  }

  def hasTitle(uuid: UUID, title: String): Boolean = getHasTitles(uuid).contains(title)

  def getHasTitles(uuid: UUID): Array[String] = {
    PlayerData.playerData(uuid).OpenedTitles match {
      case Some(titles) =>
        titles.split(";")
      case None =>
        Array.empty[String]
    }
  }

  def removeTitle(uuid: UUID, title: String): Boolean = {
    if (!hasTitle(uuid, title)) return false
    Bukkit.getOfflinePlayer(uuid).openTitles(getHasTitles(uuid).filterNot(_ == title).mkString(";") + ";")
    true
  }

  def getSelectedTitle(uuid: UUID): String = {
    PlayerData.playerData(uuid).SelectedTitle match {
      case Some(title) =>
        title
      case None =>
        null
    }
  }

  def setSelectTitle(uuid: UUID, title: String): Unit = {
    Bukkit.getOfflinePlayer(uuid).setSelectedTitle(title)
  }

  def resetSelectTitle(uuid: UUID): Unit = {
    Bukkit.getOfflinePlayer(uuid).setSelectedTitle(null)
  }

}

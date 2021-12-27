package com.ryoserver.Title

import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

class PlayerTitleData(ryoServerAssist: RyoServerAssist) {

  def openTitle(p: Player, title: String): Boolean = {
    val uuid = p.getUniqueId
    if (hasTitle(uuid, title)) return false
    new RyoServerPlayer(p).openTitle(getHasTitles(uuid).mkString(";") + (if (getHasTitles(uuid).mkString(";") != "") ";" else "") + title)
    new GiveTitle(ryoServerAssist).titleGetNumber(p)
    true
  }

  def hasTitle(uuid: UUID, title: String): Boolean = getHasTitles(uuid).contains(title)

  def getHasTitles(uuid: UUID): Array[String] = {
    Data.playerData(uuid).OpenedTitles match {
      case Some(titles) =>
        titles.split(",")
      case None =>
        Array.empty[String]
    }
  }

  def removeTitle(uuid: UUID, title: String): Boolean = {
    if (!hasTitle(uuid, title)) return false
    new RyoServerPlayer(Bukkit.getOfflinePlayer(uuid)).openTitle(getHasTitles(uuid).filterNot(_ == title).mkString(";") + ";")
    true
  }

  def getSelectedTitle(uuid: UUID): String = {
    Data.playerData(uuid).SelectedTitle match {
      case Some(title) =>
        title
      case None =>
        null
    }
  }

  def setSelectTitle(uuid: UUID, title: String): Unit = {
    new RyoServerPlayer(Bukkit.getOfflinePlayer(uuid)).setSelectedTitle(title)
  }

  def resetSelectTitle(uuid: UUID): Unit = {
    new RyoServerPlayer(Bukkit.getOfflinePlayer(uuid)).setSelectedTitle(null)
  }

}

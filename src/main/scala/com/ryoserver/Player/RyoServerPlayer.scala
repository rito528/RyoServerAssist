package com.ryoserver.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.PlayerData.playerData
import org.bukkit.OfflinePlayer

import java.util.UUID

class RyoServerPlayer(player: OfflinePlayer) {

  private val uuid: UUID = player.getUniqueId
  private var oldData: PlayerDataType = playerData(uuid)

  def giveNormalGachaTicket(amount: Int): Unit = {
    val result = oldData.copy(gachaTickets = oldData.gachaTickets + amount)
    playerData += (uuid -> result)
    oldData = result
  }

  def reduceNormalGachaTicket(amount: Int): Unit = {
    val result = oldData.copy(gachaTickets = oldData.gachaTickets - amount)
    playerData += (uuid -> result)
    oldData = result
  }

  def addOneVoteNumber(): Unit = {
    val result = oldData.copy(voteNumber = oldData.voteNumber + 1)
    playerData += (uuid -> result)
    oldData = result
  }

  def addOneContinueNumber(): Unit = {
    val result = oldData.copy(reVoteNumber = oldData.reVoteNumber + 1)
    playerData += (uuid -> result)
    oldData = result
  }

  def addGachaPullNumber(amount: Int): Unit = {
    val result = oldData.copy(gachaPullNumber = oldData.gachaPullNumber + amount)
    playerData += (uuid -> result)
    oldData = result
  }

  def updateExp(amount: Int): Unit = {
    val result = oldData.copy(exp = amount, level = new CalLv().getLevel(amount))
    playerData += (uuid -> result)
    oldData = result
  }

  def addExp(amount: Double): Unit = {
    val exp = oldData.exp + amount
    val result = oldData.copy(exp = exp, level = new CalLv().getLevel(exp))
    playerData += (uuid -> result)
    oldData = result
  }

  def toggleAutoStack(): Boolean = {
    val result = !oldData.autoStack
    val resultData = oldData.copy(autoStack = result)
    playerData += (uuid -> resultData)
    oldData = resultData
    result
  }

  def addSkillOpenPoint(amount: Int): Unit = {
    val result = oldData.copy(SkillOpenPoint = oldData.SkillOpenPoint + amount)
    playerData += (uuid -> result)
    oldData = result
  }

  def addSpecialSkillOpenPoint(amount: Int): Unit = {
    val result = oldData.copy(specialSkillOpenPoint = oldData.specialSkillOpenPoint + amount)
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }

  def skillOpen(skills: String): Unit = {
    val result = oldData.copy(OpenedSkills = Option(skills))
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }

  def specialSkillOpen(skills: String): Unit = {
    val result = oldData.copy(OpenedSpecialSkills = Option(skills))
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }

  def setLastDistributionReceived(id: Int): Unit = {
    val result = oldData.copy(lastDistributionReceived = id)
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }

  def openTitle(titles: String): Unit = {
    val result = oldData.copy(OpenedTitles = Option(titles))
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }

  def setSelectedTitle(title: String): Unit = {
    val result = oldData.copy(SelectedTitle = Option(title))
    PlayerData.playerData += (uuid -> result)
    oldData = result
  }


}

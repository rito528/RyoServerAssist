package com.ryoserver.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.Data.playerData
import org.bukkit.OfflinePlayer

import java.util.UUID

class RyoServerPlayer(player: OfflinePlayer) {

  private val uuid: UUID = player.getUniqueId
  private var oldData: PlayerData = playerData(uuid)

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
    Data.playerData += (uuid -> result)
    oldData = result
  }

  def skillOpen(skills: String): Unit = {
    val result = oldData.copy(OpenedSkills = Option(skills))
    Data.playerData += (uuid -> result)
    oldData = result
  }

  def specialSkillOpen(skills: String): Unit = {
    val result = oldData.copy(OpenedSpecialSkills = Option(skills))
    Data.playerData += (uuid -> result)
    oldData = result
  }

  def setLastDistributionReceived(id: Int): Unit = {
    val result = oldData.copy(lastDistributionReceived = id)
    Data.playerData += (uuid -> result)
    oldData = result
  }

  def openTitle(titles: String): Unit = {
    val result = oldData.copy(OpenedTitles = Option(titles))
    Data.playerData += (uuid -> result)
    oldData = result
  }

  def setSelectedTitle(title: String): Unit = {
    val result = oldData.copy(SelectedTitle = Option(title))
    Data.playerData += (uuid -> result)
    oldData = result
  }


}

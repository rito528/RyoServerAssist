package com.ryoserver.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.Data.playerData
import org.bukkit.OfflinePlayer

import java.util.UUID

class RyoServerPlayer(player: OfflinePlayer) {

  private val uuid: UUID = player.getUniqueId
  private val oldData: PlayerData = playerData(uuid)
  playerData = playerData.filterNot { case (uuid, _) => uuid == this.uuid }

  def giveNormalGachaTicket(amount: Int): Unit = {
    playerData += (uuid -> oldData.copy(gachaTickets = oldData.gachaTickets + amount))
  }

  def reduceNormalGachaTicket(amount: Int): Unit = {
    playerData += (uuid -> oldData.copy(gachaTickets = oldData.gachaTickets - amount))
  }

  def addOneVoteNumber(): Unit = {
    playerData += (uuid -> oldData.copy(voteNumber = oldData.voteNumber + 1))
  }

  def addGachaPullNumber(amount: Int): Unit = {
    playerData += (uuid -> oldData.copy(gachaPullNumber = oldData.gachaPullNumber + amount))
  }

  def updateExp(amount: Int): Unit = {
    playerData += (uuid -> oldData.copy(exp = amount, level = new CalLv().getLevel(amount)))
  }

  def addExp(amount: Int): Unit = {
    val exp = oldData.exp + amount
    playerData += (uuid -> oldData.copy(exp = exp, level = new CalLv().getLevel(exp)))
  }

  def toggleAutoStack(): Boolean = {
    val result = !oldData.autoStack
    playerData += (uuid -> oldData.copy(autoStack = result))
    result
  }

  def addSkillOpenPoint(amount: Int): Unit = {
    playerData += (uuid -> oldData.copy(SkillOpenPoint = oldData.SkillOpenPoint + amount))
  }

  def addSpecialSkillOpenPoint(amount: Int): Unit = {
    Data.playerData += (uuid -> oldData.copy(specialSkillOpenPoint = oldData.specialSkillOpenPoint + amount))
  }

  def skillOpen(skills: String): Unit = {
    Data.playerData += (uuid -> oldData.copy(OpenedSkills = Option(skills)))
  }


}

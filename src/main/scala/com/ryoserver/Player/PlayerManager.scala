package com.ryoserver.Player

import org.bukkit.entity.Player

object PlayerManager {

  implicit class getPlayerData(p:Player) {
    private val playerData: PlayerData = Data.playerData(p.getUniqueId)
    def getQuestLevel: Int = playerData.level
    def getQuestExp: Double = playerData.exp
    def getLastDistributionReceived: Int = playerData.lastDistributionReceived
    def getSkillPoint: Double = playerData.skillPoint
    def getLoginNumber: Int = playerData.loginNumber
    def getConsecutiveLoginDays: Int = playerData.consecutiveLoginDays
    def getQuestClearTimes: Int = playerData.questClearTimes
    def getGachaTickets: Int = playerData.gachaTickets
    def getGachaPullNumber: Int = playerData.gachaTickets
    def getSkillOpenPoint: Int = playerData.SkillOpenPoint
    def getOpenedSkills: Option[String] = playerData.OpenedSkills
    def getVoteNumber: Int = playerData.voteNumber
    def getSpecialSkillOpenPoint: Int = playerData.specialSkillOpenPoint
    def getOpenedSpecialSkills: Option[String] = playerData.OpenedSpecialSkills
    def getOpenedTitles: Option[String] = playerData.OpenedTitles
    def getSelectedTitle: Option[String] = playerData.SelectedTitle
    def isAutoStack: Boolean  = playerData.autoStack
  }

  implicit class setPlayerData(p:Player) {
    val rp = new RyoServerPlayer(p)
    def giveNormalGachaTickets(amount: Int) : Unit = rp.giveNormalGachaTicket(amount)
    def reduceNormalGachaTickets(amount: Int): Unit = rp.reduceNormalGachaTicket(amount)
    def addOneVoteNumber(): Unit = rp.addOneVoteNumber()
    def addGachaPullNumber(number: Int): Unit = rp.addGachaPullNumber(number)
    def questExpAddNaturally(addExp: Double): Unit = rp.addExp(addExp)
    def toggleAutoStack(): Unit = rp.toggleAutoStack()
    def addSkillOpenPoint(addPoint: Int): Unit = rp.addSkillOpenPoint(addPoint)
    def addSpecialSkillOpenPoint(addPoint:Int): Unit = rp.addSpecialSkillOpenPoint(addPoint)
    def openSkills(openedSkills: String): Unit = rp.skillOpen(openedSkills)
    def openSpecialSkills(openedSpecialSkills: String): Unit = rp.specialSkillOpen(openedSpecialSkills)
    def setLastDistributionReceived(id: Int): Unit = rp.setLastDistributionReceived(id)
    def openTitles(openedTitles: String): Unit = rp.openTitle(openedTitles)
    def setSelectedTitle(title: String): Unit = rp.setSelectedTitle(title)
  }

}

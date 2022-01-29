package com.ryoserver.Player

import com.ryoserver.SkillSystems.SkillPoint.SkillPointData
import org.bukkit.Bukkit.getLogger
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PlayerManager {

  implicit class getPlayerData(p: OfflinePlayer) {
    private val playerData: PlayerDataType = PlayerData.playerData(p.getUniqueId)

    def getQuestLevel: Int = playerData.level

    def getQuestExp: Double = playerData.exp

    def getRanking: Int = PlayerData.playerData.values.toSeq.sortBy(_.exp).reverse.indexOf(playerData) + 1

    def getBehindExpDiff: Option[Double] = {
      val nowRanking = getRanking
      if (nowRanking == PlayerData.playerData.values.toSeq.length) {
        //最下位だった場合は後ろの順位の人との差が存在しないためNoneを返す
        None
      } else {
        Option(getQuestExp - PlayerData.playerData.values.toSeq.sortBy(_.exp).reverse(nowRanking).exp)
      }
    }

    def getBeforeExpDiff: Option[Double] = {
      val nowRanking = getRanking
      if (nowRanking == 1) {
        //1位だった場合は前の順位の人との差が存在しないためNoneを返す
        None
      } else {
        Option(PlayerData.playerData.values.toSeq.sortBy(_.exp).reverse(nowRanking - 2).exp - getQuestExp)
      }
    }

    def getLastDistributionReceived: Int = playerData.lastDistributionReceived

    def getSkillPoint: Double = playerData.skillPoint

    def getLoginNumber: Int = playerData.loginNumber

    def getConsecutiveLoginDays: Int = playerData.consecutiveLoginDays

    def getQuestClearTimes: Int = playerData.questClearTimes

    def getGachaTickets: Int = playerData.gachaTickets

    def getGachaPullNumber: Int = playerData.gachaPullNumber

    def getSkillOpenPoint: Int = playerData.SkillOpenPoint

    def getOpenedSkills: Option[String] = playerData.OpenedSkills

    def getVoteNumber: Int = playerData.voteNumber

    def getReVoteNumber: Int = playerData.reVoteNumber

    def getSpecialSkillOpenPoint: Int = playerData.specialSkillOpenPoint

    def getOpenedSpecialSkills: Option[String] = playerData.OpenedSpecialSkills

    def getOpenedTitles: Option[String] = playerData.OpenedTitles

    def getSelectedTitle: Option[String] = playerData.SelectedTitle

    def isAutoStack: Boolean = playerData.autoStack
  }

  implicit class setPlayerData(p: OfflinePlayer) {
    val rp = new RyoServerPlayer(p)

    def giveNormalGachaTickets(amount: Int): Unit = rp.giveNormalGachaTicket(amount)

    def reduceNormalGachaTickets(amount: Int): Unit = rp.reduceNormalGachaTicket(amount)

    def addOneVoteNumber(): Unit = rp.addOneVoteNumber()

    def setReVoteNumber(number: Int): Unit = rp.setContinueVoteNumber(number)

    def addGachaPullNumber(number: Int): Unit = rp.addGachaPullNumber(number)

    def questExpAddNaturally(addExp: Double): Unit = rp.addExp(addExp)

    def toggleAutoStack(): Unit = rp.toggleAutoStack()

    def addSkillOpenPoint(addPoint: Int): Unit = rp.addSkillOpenPoint(addPoint)

    def addSpecialSkillOpenPoint(addPoint: Int): Unit = rp.addSpecialSkillOpenPoint(addPoint)

    def openSkills(openedSkills: String): Unit = rp.skillOpen(openedSkills)

    def openSpecialSkills(openedSpecialSkills: String): Unit = rp.specialSkillOpen(openedSpecialSkills)

    def setLastDistributionReceived(id: Int): Unit = rp.setLastDistributionReceived(id)

    def openTitles(openedTitles: String): Unit = rp.openTitle(openedTitles)

    def setSelectedTitle(title: String): Unit = rp.setSelectedTitle(title)

    def setSkillPoint(newSkillPoint: Double): Unit = {
      p match {
        case p: Player =>
          new SkillPointData().setSkillPoint(p, newSkillPoint)
        case _ =>
          getLogger.warning("オンラインではないプレイヤーに対してSkillPointを操作しています！")
      }
    }
  }

}

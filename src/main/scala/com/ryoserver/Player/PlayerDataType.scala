package com.ryoserver.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.PlayerData.PlayerDataRepository
import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkills

import java.util.{Date, UUID}

case class PlayerDataType(lastLogin: Date,lastLogout: Option[Date],
                          level: Int, exp: Double,
                          ranking: Int, lastDistributionReceived: Int,
                          skillPoint: Double, loginDays: Int,
                          consecutiveLoginDays: Int, questClearTimes: Int,
                          gachaTickets: Int, gachaPullNumber: Int,
                          skillOpenPoint: Int, openedSkills: Set[EffectSkills],
                          voteNumber: Int, reVoteNumber: Int, specialSkillOpenPoint: Int,
                          openedSpecialSkills: Set[String], openedTitles: Set[String],
                          selectedTitle: Option[String], autoStack: Boolean,
                          Twitter: Option[String], Discord: Option[String], Word: Option[String]) {

  def setExp(exp: Double): PlayerDataType = this.copy(level = new CalLv().getLevel(exp), exp = exp)

  def setRanking(ranking: Int): PlayerDataType = this.copy(ranking = ranking)

  def setLastDistributionReceived(num: Int): PlayerDataType = this.copy(lastDistributionReceived = num)

  def setSkillPoint(skillPoint: Double): PlayerDataType = this.copy(skillPoint = skillPoint)

  def setLoginNumber(loginDays: Int): PlayerDataType = this.copy(loginDays = loginDays)

  def setConsecutiveLoginDays(consecutiveLoginNum: Int): PlayerDataType = this.copy(consecutiveLoginDays = consecutiveLoginNum)

  def setQuestClearTimes(questClearTimes: Int): PlayerDataType = this.copy(questClearTimes = questClearTimes)

  def setGachaTickets(gachaTickets: Int): PlayerDataType = this.copy(gachaTickets = gachaTickets)

  def setGachaPullNumber(gachaPullNumber: Int): PlayerDataType = this.copy(gachaPullNumber = gachaPullNumber)

  def setSkillOpenPoint(skillOpenPoint: Int): PlayerDataType = this.copy(skillOpenPoint = skillOpenPoint)

  def addOpenedSkills(effectSkills: EffectSkills): PlayerDataType = this.copy(openedSkills = openedSkills ++ Set(effectSkills))

  def addVoteNumber(addVoteNum: Int): PlayerDataType = this.copy(voteNumber = voteNumber + addVoteNum)

  def setReVoteNumber(reVoteNum: Int): PlayerDataType = this.copy(reVoteNumber = reVoteNum)

  def setSpecialSkillOpenPoint(specialSkillOpenPoint: Int): PlayerDataType = this.copy(specialSkillOpenPoint = specialSkillOpenPoint)

  def addOpenedSpecialSkill(specialSkill: String): PlayerDataType = this.copy(openedSpecialSkills = openedSpecialSkills ++ Set(specialSkill))

  def addOpenedTitles(title: String): PlayerDataType = this.copy(openedTitles = openedTitles ++ Set(title))

  def selectedTitle(title: Option[String]): PlayerDataType = this.copy(selectedTitle = title)

  def toggleAutoStack(): PlayerDataType = this.copy(autoStack = !autoStack)

  def apply(implicit uuid: UUID): Unit = new PlayerDataRepository().updateData(uuid,this)

}

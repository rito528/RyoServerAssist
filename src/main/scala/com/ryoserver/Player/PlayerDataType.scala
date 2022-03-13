package com.ryoserver.Player

import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkills

case class PlayerDataType(level: Int, exp: Double,
                          lastDistributionReceived: Int, skillPoint: Double,
                          loginNumber: Int, consecutiveLoginDays: Int,
                          questClearTimes: Int, gachaTickets: Int,
                          gachaPullNumber: Int, SkillOpenPoint: Int,
                          OpenedSkills: Set[EffectSkills], voteNumber: Int,
                          reVoteNumber: Int, specialSkillOpenPoint: Int,
                          OpenedSpecialSkills: Option[String], OpenedTitles: Option[String],
                          SelectedTitle: Option[String], autoStack: Boolean,
                          Twitter: Option[String], Discord: Option[String],
                          Word: Option[String]) {

  def setLevel(level: Int): PlayerDataType = this.copy(level = level)

  def setExp(exp: Double): PlayerDataType = this.copy(exp = exp)

  def setLastDistributionReceived(num: Int): PlayerDataType = this.copy(lastDistributionReceived = num)

  def setSkillPoint(skillPoint: Double): PlayerDataType = this.copy(skillPoint = skillPoint)

  def setLoginNumber(loginNumber: Int): PlayerDataType = this.copy(loginNumber = loginNumber)

  def setConsecutiveLoginDays(consecutiveLoginNum: Int): PlayerDataType = this.copy(consecutiveLoginDays = consecutiveLoginNum)

  def setQuestClearTimes(questClearTimes: Int): PlayerDataType = this.copy(questClearTimes = questClearTimes)
}

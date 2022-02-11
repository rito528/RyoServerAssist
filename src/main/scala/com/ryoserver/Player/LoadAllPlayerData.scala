package com.ryoserver.Player

import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkills
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID
import scala.collection.mutable

class LoadAllPlayerData() {

  def load(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    PlayerData.playerData = mutable.Map.empty
    sql"SELECT * FROM Players ORDER BY EXP DESC;".foreach(rs => {
      val uuid = UUID.fromString(rs.string("UUID"))
      val level = rs.int("Level")
      val exp = rs.double("EXP")
      val lastDistributionReceived = rs.int("lastDistributionReceived")
      val skillPoint = rs.double("SkillPoint")
      val loginNumber = rs.int("loginDays")
      val consecutiveLoginDays = rs.int("consecutiveLoginDays")
      val questClearTimes = rs.int("questClearTimes")
      val gachaTickets = rs.int("gachaTickets")
      val gachaPullNumber = rs.int("gachaPullNumber")
      val skillOpenPoint = rs.int("SkillOpenPoint")
      val OpenedSkills: Set[EffectSkills] = {
        val skills = rs.string("OpenedSkills")
        if (skills != null) {
          skills
          .split(";")
          .map(skillName => EffectSkills.valuesToIndex.filter(_._1.skillName == skillName).head._1)
          .toSet
        } else {
          Set.empty
        }
      }

      val voteNumber = rs.int("VoteNumber")
      val ContinueVoteNumber = rs.int("ContinueVoteNumber")
      val specialSkillOpenPoint = rs.int("SpecialSkillOpenPoint")
      val openedSpecialSkills = rs.string("OpenedSpecialSkills")
      val openedTitles = rs.string("OpenedTitles")
      val selectedTitles = rs.string("SelectedTitle")
      val autoStack = rs.boolean("autoStack")
      val twitter = rs.string("Twitter")
      val discord = rs.string("Discord")
      val word = rs.string("Word")
      PlayerData.playerData += (uuid -> PlayerDataType(level, exp, lastDistributionReceived, skillPoint, loginNumber, consecutiveLoginDays,
        questClearTimes, gachaTickets, gachaPullNumber, skillOpenPoint, OpenedSkills, voteNumber, ContinueVoteNumber, specialSkillOpenPoint, Option(openedSpecialSkills),
        Option(openedTitles), Option(selectedTitles), autoStack, Option(twitter), Option(discord), Option(word)))
    })
  }

}

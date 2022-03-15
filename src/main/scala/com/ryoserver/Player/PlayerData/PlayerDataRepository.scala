package com.ryoserver.Player.PlayerData
import com.ryoserver.Distribution.DistributionData
import com.ryoserver.Player.PlayerDataType
import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkills
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.util.ScalikeJDBC.getData
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

class PlayerDataRepository extends TPlayerDataRepository {

  private implicit val session: AutoSession.type = AutoSession

  override def store(uuid: UUID): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val playerData = PlayerDataEntity.playerData(uuid)
    sql"""UPDATE Players SET
         last_login=${format.format(playerData.lastLogin)},
         last_logout=${format.format(playerData.lastLogout)},
         level=${playerData.level},
         exp=${playerData.exp},
         last_distribution_received=${playerData.lastDistributionReceived},
         skill_point=${playerData.skillPoint},
         login_days=${playerData.loginDays},
         consecutive_login_days=${playerData.consecutiveLoginDays},
         quest_clear_times=${playerData.questClearTimes},
         gacha_tickets=${playerData.questClearTimes},
         gacha_pull_number=${playerData.gachaPullNumber},
         skill_open_point=${playerData.skillOpenPoint},
         opened_skills=${playerData.openedSkills.map(_.skillName).mkString(";")},
         vote_number=${playerData.voteNumber},
         continue_vote_number=${playerData.reVoteNumber},
         special_skill_open_point=${playerData.specialSkillOpenPoint},
         opened_special_skills=${playerData.openedSpecialSkills.mkString(";")},
         opened_titles=${playerData.openedTitles.mkString(";")},
         selected_title=${playerData.selectedTitle},
         is_auto_stack=${playerData.autoStack},
         Twitter=${playerData.Twitter},
         Discord=${playerData.Discord},
         Word=${playerData.Word}"""
      .execute()
      .apply()
  }

  override def restore(uuid: UUID): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val playersTable = sql"SELECT * FROM Players WHERE UUID=$uuid"
    val playerData: PlayerDataType = {
      if (playersTable.getHeadData.nonEmpty) {
        playersTable.map(rs => {
          PlayerDataType(
            lastLogin = format.parse(rs.string("last_login")),
            lastLogout = Option(format.parse(rs.string("last_logout"))),
            level = rs.int(rs.string("level")),
            exp = rs.double("exp"),
            lastDistributionReceived = rs.int("last_distribution_received"),
            skillPoint = rs.double("skill_point"),
            loginDays = rs.int("login_days"),
            consecutiveLoginDays = rs.int("consecutive_login_days"),
            questClearTimes = rs.int("quest_clear_times"),
            gachaTickets = rs.int("gacha_tickets"),
            gachaPullNumber = rs.int("gacha_pull_number"),
            skillOpenPoint = rs.int("skill_open_point"),
            openedSkills = rs.string("opened_skills").split(';').map(stringName => EffectSkills.values.filter(_.skillName == stringName).head).toSet,
            voteNumber = rs.int("vote_number"),
            reVoteNumber = rs.int("continue_vote_number"),
            specialSkillOpenPoint = rs.int("special_skill_open_point"),
            openedSpecialSkills = rs.string("opened_special_skills").split(';').toSet,
            openedTitles = rs.string("opened_titles").split(';').toSet,
            selectedTitle = rs.stringOpt("selected_title"),
            autoStack = rs.boolean("is_auto_stack"),
            Twitter = rs.stringOpt("Twitter"),
            Discord = rs.stringOpt("Discord"),
            Word = rs.stringOpt("Word")
          )
        }).headOption().apply().get
      } else {
        PlayerDataType(
          lastLogin = new Date,
          lastLogout = None,
          level = 0,
          exp = 0,
          lastDistributionReceived = DistributionData.distributionData.maxBy(_.id).id,
          skillPoint = new SkillPointCal().getMaxSkillPoint(0),
          loginDays = 1,
          consecutiveLoginDays = 0,
          questClearTimes = 0,
          gachaTickets = 0,
          gachaPullNumber = 0,
          skillOpenPoint = 0,
          openedSkills = Set.empty,
          voteNumber = 0,
          reVoteNumber = 0,
          specialSkillOpenPoint = 0,
          openedSpecialSkills = Set.empty,
          openedTitles = Set.empty,
          selectedTitle = None,
          autoStack = false,
          Twitter = None,
          Discord = None,
          Word = None
        )
      }
    }
    PlayerDataEntity.playerData += uuid -> playerData
  }

}

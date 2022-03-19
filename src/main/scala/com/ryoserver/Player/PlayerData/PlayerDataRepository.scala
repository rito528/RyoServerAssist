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

  override def store(): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    PlayerDataEntity.playerData.foreach{case (uuid,playerData) =>
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
         Word=${playerData.Word}
         WHERE uuid=$uuid"""
        .execute()
        .apply()
    }
  }

  override def restore(uuid: UUID): Unit = {
    if (PlayerDataEntity.playerData.contains(uuid)) return
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val playersTable = sql"SELECT *,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking,COUNT(*) AS max_row_num FROM Players WHERE UUID=$uuid;"
    val playerData: PlayerDataType = {
      if (playersTable.getHeadData.nonEmpty) {
        playersTable.map(rs => {
          val nextPlayerExp = if (rs.int("ranking") != 1) sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=${rs.int("ranking") + 1};"
            .getHeadData.get("exp").toString.toInt else 0
          val backPlayerExp = if (rs.int("max_row_num") != rs.int("ranking")) sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=${rs.int("ranking") - 1};"
            .getHeadData.get("exp").toString.toInt else 0
          PlayerDataType(
            lastLogin = format.parse(rs.string("last_login")),
            lastLogout = Option(format.parse(rs.string("last_logout"))),
            level = rs.int(rs.string("level")),
            exp = rs.double("exp"),
            ranking = rs.int("ranking"),
            nextRankingExpDiff = if (rs.int("ranking") == 1) 0 else nextPlayerExp - rs.double("exp"),
            backRankingExpDiff = if (rs.int("max_row_num") != rs.int("ranking")) rs.double("exp") - backPlayerExp else 0,
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
        sql"SELECT COUNT(*) AS max_row_num FROM Players;".map(rs => {
          val nextPlayerExp = sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=${rs.int("ranking") + 1};"
            .getHeadData.get("exp").toString.toInt
          PlayerDataType(
            lastLogin = new Date,
            lastLogout = None,
            level = 0,
            exp = 0,
            nextRankingExpDiff = nextPlayerExp - rs.double("exp"),
            backRankingExpDiff = 0,
            ranking = rs.int("max_row_num") + 1,
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
        }).headOption().apply().get
      }
    }
    updateData(uuid,playerData)
  }

  override def findBy(uuid: UUID): Option[PlayerDataType] = {
    if (PlayerDataEntity.playerData.contains(uuid)) Option(PlayerDataEntity.playerData(uuid))
    else None
  }

  override def updateData(uuid: UUID, playerDataType: PlayerDataType): Unit = {
    PlayerDataEntity.playerData += uuid -> playerDataType
  }

}

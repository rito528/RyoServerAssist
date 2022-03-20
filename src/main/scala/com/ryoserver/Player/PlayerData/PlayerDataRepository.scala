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
      val openedSkills = Option(
        playerData.openedSkills match {
          case Some(skills) => skills.map(_.skillName).mkString(";")
          case None => null
        }
      )
      val openedSpecialSkills = Option(
        playerData.openedSpecialSkills match {
          case Some(specialSkills) => specialSkills.mkString(";")
          case None => null
        }
      )
      val openedTitles = Option(
        playerData.openedTitles match {
          case Some(titles) => titles.mkString(";")
          case None => null
        }
      )
      sql"""INSERT INTO Players
            (uuid,last_login,last_logout,level,exp,last_distribution_received,skill_point,
            login_days,consecutive_login_days,quest_clear_times,gacha_tickets,gacha_pull_number,
            skill_open_point,opened_skills,vote_number,continue_vote_number,last_vote,special_skill_open_point,
            opened_special_skills,opened_titles,selected_title,is_auto_stack,last_daily_quest_time,Twitter,Discord,Word)
          VALUES (${uuid.toString},${format.format(playerData.lastLogin)},${format.format(playerData.lastLogout.getOrElse(new Date))},${playerData.level},
         ${playerData.exp},${playerData.lastDistributionReceived},${playerData.skillPoint},${playerData.loginDays},${playerData.consecutiveLoginDays},
         ${playerData.questClearTimes},${playerData.gachaTickets},${playerData.gachaPullNumber},${playerData.skillOpenPoint},
         $openedSkills,${playerData.voteNumber},${playerData.reVoteNumber},${format.format(playerData.lastVote)},${playerData.specialSkillOpenPoint},
         $openedSpecialSkills,$openedTitles,${playerData.selectedTitle},${playerData.autoStack},${format.format(playerData.lastDailyQuestDate)},
         ${playerData.Twitter}, ${playerData.Discord},${playerData.Word})
         ON DUPLICATE KEY UPDATE
         last_login=VALUES(last_login),last_logout=VALUES(last_logout),level=VALUES(level),exp=VALUES(exp),
         last_distribution_received=VALUES(last_distribution_received),skill_point=VALUES(skill_point),login_days=VALUES(login_days),
         consecutive_login_days=VALUES(consecutive_login_days),quest_clear_times=VALUES(quest_clear_times),gacha_tickets=VALUES(gacha_tickets),
         gacha_pull_number=VALUES(gacha_pull_number),skill_open_point=VALUES(skill_open_point),opened_skills=VALUES(opened_skills),
         vote_number=VALUES(vote_number),continue_vote_number=VALUES(continue_vote_number),last_vote=VALUES(last_vote),special_skill_open_point=VALUES(special_skill_open_point),
         opened_special_skills=VALUES(opened_special_skills),opened_titles=VALUES(opened_titles),selected_title=VALUES(selected_title),
         is_auto_stack=VALUES(is_auto_stack),last_daily_quest_time=VALUES(last_daily_quest_time),Twitter=VALUES(Twitter),Discord=VALUES(Discord),Word=VALUES(Word)
         """
        .execute()
        .apply()
    }
  }

  override def restore(uuid: UUID): Unit = {
      if (PlayerDataEntity.playerData.contains(uuid)) return
      val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val playersTable = sql"SELECT *,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking,COUNT(*) AS max_row_num FROM Players WHERE UUID=${uuid.toString};"
      val playerData: PlayerDataType = {
        if (playersTable.getHeadData.getOrElse(Map.empty).contains("uuid")) {
          playersTable.map(rs => {
            val nextPlayerExp = if (rs.int("ranking") != 1) {
              val next = sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=${rs.int("ranking") - 1};"
              next.getHeadData.get("exp").toString.toInt
            } else {
              0
            }
            val backPlayerExp = if (rs.int("max_row_num") != rs.int("ranking")) {
              val back = sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=${rs.int("ranking") + 1};"
              back.getHeadData.get("exp").toString.toInt
            } else {
              0
            }
            PlayerDataType(
              lastLogin = format.parse(rs.string("last_login")),
              lastLogout = Option(format.parse(rs.string("last_logout"))),
              level = rs.int("level"),
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
              openedSkills = Option(
                rs.stringOpt("opened_skills") match {
                  case Some(skills) =>
                    if (skills != "") skills.split(';').map(skillName => EffectSkills.values.filter(_.skillName == skillName).head).toSet
                    else null
                  case None =>
                    null
                }
              ),
              voteNumber = rs.int("vote_number"),
              reVoteNumber = rs.int("continue_vote_number"),
              lastVote = format.parse(rs.string("last_vote")),
              specialSkillOpenPoint = rs.int("special_skill_open_point"),
              openedSpecialSkills = Option(rs.stringOpt("opened_special_skills") match {
                case Some(skills) =>
                  if (skills != "") skills.split(';').toSet
                  else null
                case None =>
                  null
              }),
              openedTitles = Option(rs.stringOpt("opened_titles") match {
                case Some(titles) =>
                  if (titles != "") titles.split(';').toSet
                  else null
                case None =>
                  null
              }),
              selectedTitle = rs.stringOpt("selected_title"),
              autoStack = rs.boolean("is_auto_stack"),
              lastDailyQuestDate = format.parse(rs.string("last_daily_quest_time")),
              Twitter = rs.stringOpt("Twitter"),
              Discord = rs.stringOpt("Discord"),
              Word = rs.stringOpt("Word")
            )
          }).headOption().apply().get
        } else {
          sql"SELECT COUNT(*) AS max_row_num FROM Players;".map(rs => {
            val nextPlayerExpSQL = sql"SELECT exp,(SELECT COUNT(*) + 1 FROM Players B WHERE B.exp > Players.exp) AS ranking FROM Players HAVING ranking=ranking-1;"
            val nextPlayerExp = {
              nextPlayerExpSQL.getHeadData match {
                case Some(data) => data("exp").toString.toInt
                case None => 0
              }
            }
            val exp = nextPlayerExpSQL.map(_.doubleOpt("exp").getOrElse(0.0)).headOption().apply().getOrElse(0.0)
            PlayerDataType(
              lastLogin = new Date,
              lastLogout = None,
              level = 0,
              exp = 0,
              nextRankingExpDiff = nextPlayerExp - exp,
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
              openedSkills = None,
              voteNumber = 0,
              reVoteNumber = 0,
              lastVote = format.parse("2022-01-01 00:00:00"),
              specialSkillOpenPoint = 0,
              openedSpecialSkills = None,
              openedTitles = None,
              selectedTitle = None,
              autoStack = false,
              lastDailyQuestDate = format.parse("2022-01-01 00:00:00"),
              Twitter = None,
              Discord = None,
              Word = None
            )
          }).headOption().apply().get
        }
      }
      updateData(uuid, playerData)
  }

  override def findBy(uuid: UUID): Option[PlayerDataType] = {
    if (PlayerDataEntity.playerData.contains(uuid)) Option(PlayerDataEntity.playerData(uuid))
    else None
  }

  override def updateData(uuid: UUID, playerDataType: PlayerDataType): Unit = {
    PlayerDataEntity.playerData += (uuid -> playerDataType)
  }

}

package com.ryoserver.Player

import com.ryoserver.util.SQL

import java.util.UUID
import scala.collection.mutable

class LoadAllPlayerData() {

  def load(): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery("SELECT * FROM Players ORDER BY EXP DESC;")
    PlayerData.playerData = mutable.Map.empty
    while (rs.next()) {
      val uuid = UUID.fromString(rs.getString("UUID"))
      val level = rs.getInt("Level")
      val exp = rs.getDouble("EXP")
      val lastDistributionReceived = rs.getInt("lastDistributionReceived")
      val skillPoint = rs.getDouble("SkillPoint")
      val loginNumber = rs.getInt("loginDays")
      val consecutiveLoginDays = rs.getInt("consecutiveLoginDays")
      val questClearTimes = rs.getInt("questClearTimes")
      val gachaTickets = rs.getInt("gachaTickets")
      val gachaPullNumber = rs.getInt("gachaPullNumber")
      val skillOpenPoint = rs.getInt("SkillOpenPoint")
      val OpenedSkills = rs.getString("OpenedSkills")
      val voteNumber = rs.getInt("VoteNumber")
      val ContinueVoteNumber = rs.getInt("ContinueVoteNumber")
      val specialSkillOpenPoint = rs.getInt("SpecialSkillOpenPoint")
      val openedSpecialSkills = rs.getString("OpenedSpecialSkills")
      val openedTitles = rs.getString("OpenedTitles")
      val selectedTitles = rs.getString("SelectedTitle")
      val autoStack = rs.getBoolean("autoStack")
      val twitter = rs.getString("Twitter")
      val discord = rs.getString("Discord")
      val word = rs.getString("Word")
      PlayerData.playerData += (uuid -> PlayerDataType(level, exp, lastDistributionReceived, skillPoint, loginNumber, consecutiveLoginDays,
        questClearTimes, gachaTickets, gachaPullNumber, skillOpenPoint, Option(OpenedSkills), voteNumber, ContinueVoteNumber, specialSkillOpenPoint, Option(openedSpecialSkills),
        Option(openedTitles), Option(selectedTitles), autoStack, Option(twitter), Option(discord), Option(word)))
    }
    sql.close()
  }

}

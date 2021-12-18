package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

class LoadPlayerData(ryoServerAssist: RyoServerAssist) {

  def autoLoad(): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        val sql = new SQL(ryoServerAssist)
        val rs = sql.executeQuery("SELECT * FROM Players ORDER BY EXP DESC;")
        var ranking = 1
        Data.playerData = mutable.Map.empty
        while (rs.next()) {
          val uuid = rs.getString("UUID")
          val level = rs.getInt("Level")
          val exp = rs.getInt("EXP")
          val skillPoint = rs.getDouble("SkillPoint")
          val loginNumber = rs.getInt("loginDays")
          val consecutiveLoginDays = rs.getInt("consecutiveLoginDays")
          val questClearTimes = rs.getInt("questClearTimes")
          val gachaPullNumber = rs.getInt("gachaPullNumber")
          val voteNumber = rs.getInt("VoteNumber")
          Data.playerData += (uuid -> PlayerData(level, exp, skillPoint, ranking, loginNumber, consecutiveLoginDays, questClearTimes, gachaPullNumber, voteNumber))
          ranking += 1
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,oneMinute)
  }

}

package com.ryoserver.Player

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.apache.commons.lang.time.DateUtils
import org.bukkit.entity.Player

import java.util.{Calendar, Date}

class UpdateData {

  def updateReLogin(implicit p: Player): Unit = {
    val lastVoteDay = DateUtils.truncate(p.getRyoServerData.lastLogin,Calendar.DAY_OF_MONTH).getTime
    val nowDay = DateUtils.truncate(new Date,Calendar.DAY_OF_MONTH).getTime
    val oneWeekMilliSec = 604800000 //1週間をミリ秒で表す
    val ryoServerData = p.getRyoServerData
    if (nowDay - lastVoteDay == 0) return
    if (nowDay - lastVoteDay <= oneWeekMilliSec) { //1週間以内だったら
      ryoServerData.setConsecutiveLoginDays(ryoServerData.consecutiveLoginDays + 1).apply
    } else {
      ryoServerData.setConsecutiveLoginDays(0).apply
    }
  }

}

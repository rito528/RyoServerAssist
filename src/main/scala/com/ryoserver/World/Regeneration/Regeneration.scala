package com.ryoserver.World.Regeneration

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import org.bukkit.World.Environment

import java.util.{Calendar, TimeZone}

class Regeneration {

  def regeneration(implicit ryoServerAssist: RyoServerAssist, isForce: Boolean = false): Unit = {
    if ((!isFriday && !isForce) || (!isForce && !getConfig.autoWorldRegeneration)) return
    ryoServerAssist.getLogger.info("ワールドの再生成を行います。")
    new RegenerationTask(ryoServerAssist,getConfig.regenerationNormalWorlds, Environment.NORMAL).start()
    new RegenerationTask(ryoServerAssist,getConfig.regenerationNetherWorlds, Environment.NETHER).start()
    new RegenerationTask(ryoServerAssist,getConfig.regenerationEndWorlds, Environment.THE_END).start()
  }

  private def isFriday: Boolean = {
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    if (calendar.get(Calendar.DAY_OF_WEEK) == 6) return true
    false
  }

}

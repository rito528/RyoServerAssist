package com.ryoserver.World.Regeneration

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Home.HomeData
import com.ryoserver.RyoServerAssist
import org.bukkit.World.Environment
import org.bukkit.scheduler.BukkitRunnable

import java.util.{Calendar, TimeZone}

class Regeneration {

  def regeneration(implicit ryoServerAssist: RyoServerAssist, isForce: Boolean = false): Unit = {
    if ((!isFriday && !isForce) || (!isForce && !getConfig.autoWorldRegeneration)) return
    ryoServerAssist.getLogger.info("ワールドの再生成を行います。")
    new RegenerationTask(ryoServerAssist,getConfig.regenerationNormalWorlds, Environment.NORMAL).runRegeneration()
    new BukkitRunnable {
      override def run(): Unit = {
        new RegenerationTask(ryoServerAssist, getConfig.regenerationNetherWorlds, Environment.NETHER).runRegeneration()
      }
    }.runTaskLater(ryoServerAssist,20 * 30)
    new BukkitRunnable {
      override def run(): Unit = {
        new RegenerationTask(ryoServerAssist,getConfig.regenerationEndWorlds, Environment.THE_END).runRegeneration()
      }
    }.runTaskLater(ryoServerAssist,20 * 60)
  }

  private def isFriday: Boolean = {
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    if (calendar.get(Calendar.DAY_OF_WEEK) == 6) return true
    false
  }

}

package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable

class SavePlayerData(ryoServerAssist: RyoServerAssist) {

  def autoSave(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,1200)
  }

  def save(): Unit = {
    val sql = new SQL(ryoServerAssist)
    Data.playerData.foreach{case (uuid,data) =>
      sql.executeSQL(s"UPDATE Players SET SkillPoint=${data.skillPoint},SpecialSkillOpenPoint=${data.specialSkillOpenPoint},OpenedSpecialSkills='${data.OpenedSpecialSkills}' WHERE UUID='${uuid}'")
    }
  }

}

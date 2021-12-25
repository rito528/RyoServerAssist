package com.ryoserver.Distribution

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable

class SaveDistribution(ryoServerAssist: RyoServerAssist) {

  def save(): Unit = {
    val sql = new SQL(ryoServerAssist)
    DistributionData.addedList.foreach(list => {
      val data = DistributionData.distributionData(list)
      sql.executeSQL(s"INSERT INTO Distribution (GachaPaperType,Count) VALUES ('${data.TicketType}',${data.amount})")
    })
    sql.close()
  }

  def autoSave(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,1200)
  }

}

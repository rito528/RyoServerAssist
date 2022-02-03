package com.ryoserver.Distribution

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class SaveDistribution(implicit ryoServerAssist: RyoServerAssist) {

  def autoSave(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 1200)
  }

  def save(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    DistributionData.addedList.reverse.foreach(list => {
      val data = DistributionData.distributionData.reverse(list - 1)
      sql"INSERT INTO Distribution (GachaPaperType,Count) VALUES (${data.TicketType},${data.amount})".execute.apply()
    })
    DistributionData.addedList = List.empty
  }

}

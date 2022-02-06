package com.ryoserver.Maintenance

import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.Bukkit
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

object MaintenanceData {

  private var isMaintenance = false

  def loadMaintenance(): Unit = {
    implicit val autoSession: AutoSession.type  = AutoSession
    val maintenanceTable = sql"SELECT * FROM ServerMaintenance"
    maintenanceTable.foreach(rs => {
      if (maintenanceTable.getHeadData.isEmpty) {
        sql"INSERT INTO ServerMaintenance (isMaintenance) VALUES (false);".execute().apply()
      } else {
        isMaintenance = rs.boolean("isMaintenance")
      }
    })
  }

  def setMaintenance(isEnable: Boolean): Unit = {
    implicit val autoSession: AutoSession.type = AutoSession
    sql"UPDATE ServerMaintenance SET isMaintenance=$isEnable".execute().apply()
    isMaintenance = isEnable
    if (isMaintenance) {
      Bukkit.getOnlinePlayers.forEach(p => {
        if (!p.hasPermission("ryoserverassist.maintenance")) {
          p.kickPlayer("サーバーがメンテナンスモードに切り替わりました。")
        }
      })
    }
  }

  def getMaintenance: Boolean = isMaintenance

}

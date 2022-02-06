package com.ryoserver.Maintenance

import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

object MaintenanceData {

  private var isMaintenance = false

  def loadMaintenance(): Unit = {
    implicit val autoSession: AutoSession.type  = AutoSession
    sql"SELECT * FROM ServerMaintenance".foreach(rs => {
      isMaintenance = rs.boolean("isMaintenance")
    })
  }

  def setMaintenance(isEnable: Boolean): Unit = {
    implicit val autoSession: AutoSession.type = AutoSession
    sql"UPDATE ServerMaintenance SET isMaintenance=$isEnable".execute().apply()
    isMaintenance = isEnable
  }

  def getMaintenance: Boolean = isMaintenance

}

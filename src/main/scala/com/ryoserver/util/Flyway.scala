package com.ryoserver.util

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.util.ScalikeJDBC.{PASS, USER}
import org.flywaydb.core.Flyway

object Flyway {

  def migrate(): Unit = {
    val flyway: Flyway = org.flywaydb.core.Flyway.configure().dataSource(s"jdbc:mysql://${getConfig.host}/",USER,PASS)
      .locations(getClass.getClassLoader.getResource("db/migrate").getPath).load()
    flyway.migrate()
  }

}

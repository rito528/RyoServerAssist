package com.ryoserver.util

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import org.flywaydb.core.Flyway
import scalikejdbc._

object ScalikeJDBC {

  private val driver = "com.mysql.cj.jdbc.Driver"
  private val URL = s"jdbc:mysql://${getConfig.host}/${getConfig.db}?useSSL=false"
  private val USER = getConfig.user
  private val PASS = getConfig.pw

  implicit val session: AutoSession.type = AutoSession

  def setup(): Unit = {
    Class.forName(this.driver)
    val settings = ConnectionPoolSettings(
      initialSize = 5,
      maxSize = 1000,
      connectionTimeoutMillis = 3000L,
      validationQuery = "SELECT 1 from dual"
    )
    ConnectionPool.singleton(this.URL,this.USER,this.PASS,settings)
  }

  implicit class getData(sql: SQL[Nothing, NoExtractor]) {
    def getHeadData: Option[Map[String, Any]] = {
      sql.map(rs => rs.toMap()).headOption.apply()
    }
  }

  def migrate(): Unit = {
    org.flywaydb.core.Flyway.configure().dataSource(s"jdbc:mysql://${getConfig.host}:3306/RyoServerAssist", USER, PASS)
      .locations(s"filesystem:${System.getProperty("user.dir")}/plugins/RyoServerAssist/db/migration")
      .baselineOnMigrate(true)
      .load()
      .migrate()
  }

}

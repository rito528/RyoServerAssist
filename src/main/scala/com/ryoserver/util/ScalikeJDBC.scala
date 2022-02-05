package com.ryoserver.util

import com.ryoserver.Config.ConfigData.getConfig
import scalikejdbc._

object ScalikeJDBC {

  final val driver = "com.mysql.cj.jdbc.Driver"
  final val URL = s"jdbc:mysql://${getConfig.host}/${getConfig.db}?autoReconnect=true&useSSL=false"
  final val USER = getConfig.user
  final val PASS = getConfig.pw

  implicit val session: AutoSession.type = AutoSession

  def setup(): Unit = {
    Class.forName(this.driver)
    ConnectionPool.singleton(this.URL, this.USER, this.PASS)
  }

  implicit class getData(sql: SQL[Nothing, NoExtractor]) {
    def getHeadData: Option[Map[String, Any]] = {
      sql.map(rs => rs.toMap()).headOption.apply()
    }
  }

}

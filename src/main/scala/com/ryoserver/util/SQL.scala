package com.ryoserver.util

import com.ryoserver.Config.ConfigData.getConfig

import java.sql._

class SQL {

  private val driver = "com.mysql.cj.jdbc.Driver"
  private val URL = s"jdbc:mysql://${getConfig.host}/${getConfig.db}?autoReconnect=true&useSSL=false"
  private val USER = getConfig.user
  private val PASS = getConfig.pw
  private var con: Connection = _
  private var rs: ResultSet = _
  private var ps: PreparedStatement = _

  def connectionTest(): Boolean = {
    try {
      Class.forName(this.driver)
      this.con = DriverManager.getConnection(this.URL, this.USER, this.PASS)
      this.con.close()
      true
    } catch {
      case _: Exception => false
    }
  }

  def executeQuery(query: String): ResultSet = {
    Class.forName(this.driver)
    this.con = DriverManager.getConnection(this.URL, this.USER, this.PASS)
    this.ps = this.con.prepareStatement(query)
    this.rs = this.ps.executeQuery()
    rs
  }

  def executeQueryPurseFolder(query: String, purseFolder: String): ResultSet = {
    Class.forName(this.driver)
    this.con = DriverManager.getConnection(this.URL, this.USER, this.PASS)
    this.ps = this.con.prepareStatement(query)
    ps.setString(1, purseFolder)
    this.rs = this.ps.executeQuery()
    rs
  }

  def executeSQL(sql: String): Unit = {
    Class.forName(this.driver)
    this.con = DriverManager.getConnection(this.URL, this.USER, this.PASS)
    this.ps = this.con.prepareStatement(sql)
    this.ps.executeUpdate()
    this.ps.close()
    this.con.close()
  }

  def createTable(tableName: String,columnData: List[ColumnData]): Unit = {
    //テーブルが存在するかチェック
    val rs = executeQuery(s"SHOW TABLES LIKE '$tableName';")
    if (rs.next()) {
      //存在する
    } else {
      //存在しないのでそのままcreate文発行
      //カラムの情報を組み立てる
      val sb = new StringBuilder
      columnData.foreach(data => {
        if (columnData.head != data) sb.append(",")
        sb.append(s"${data.columnName} ${data.dataType} ${if (data.isPrimaryKey) "AUTO INCREMENT" else ""} ${data.option}")
      })
      executeSQL(s"CREATE TABLE IF NOT EXISTS $tableName(${sb.toString()})")
    }
  }

  def purseFolder(sql: String, quote: String): Unit = {
    Class.forName(this.driver)
    this.con = DriverManager.getConnection(this.URL, this.USER, this.PASS)
    this.ps = this.con.prepareStatement(sql)
    ps.setString(1, quote)
    if (sql.split('?').length == 3) ps.setString(2, quote)
    this.ps.executeUpdate()
    this.ps.close()
    this.con.close()
  }

  def close(): Unit = {
    if (this.con != null) this.con.close()
    if (this.ps != null) this.ps.close()
    if (this.rs != null) this.rs.close()
  }

}
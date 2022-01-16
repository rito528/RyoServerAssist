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
      //カラムが存在するので不足しているカラムがないか確認
      columnData.zipWithIndex.foreach{case (data,index) => {
        val checkColumn = executeQuery(s"DESCRIBE $tableName ${data.columnName}")
        if (!checkColumn.next()) {
          //カラムが存在しない
          executeSQL(s"ALTER TABLE $tableName ADD ${data.columnName} ${data.dataType} ${if (data.option != null) data.option else ""}${if (index == 0 )"" else s" AFTER ${columnData(index - 1).columnName}"}")
        } else if (checkColumn.getString("Type") != data.dataType) {
          //カラムが存在するけど型が違うので変更する
          executeSQL(s"ALTER TABLE $tableName MODIFY ${data.columnName} ${data.dataType}")
        }
      }
      }
    } else {
      //存在しないのでそのままcreate文発行
      //カラムの情報を組み立てる
      val sb = new StringBuilder
      columnData.foreach(data => {
        if (columnData.head != data) sb.append(",")
        sb.append(s"${data.columnName} ${data.dataType}${if (data.isPrimaryKey && data.dataType.equalsIgnoreCase("INT")) " AUTO_INCREMENT" else ""}${if (data.option != null) s" ${data.option}" else ""}")
      })
      if (columnData.exists(_.isPrimaryKey == true)) {
        val column = columnData.filter(_.isPrimaryKey).head
        sb.append(s",PRIMARY KEY(${column.columnName}${if (!column.dataType.equalsIgnoreCase("INT")) "(64)" else ""})")
      }
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
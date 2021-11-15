package com.ryoserver.File

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

import java.sql.SQLException
import scala.io.Source

class Patch(ryoServerAssist: RyoServerAssist) {

  def getAndExecutePatch(): Unit = {
    ryoServerAssist.getLogger.info("パッチの実行中...")
    val sql = new SQL(ryoServerAssist)
    List(
      "1.4.0_SQLPatch.rp"
    ).foreach(f => {
      val is = getClass.getClassLoader.getResourceAsStream("patch/" + f)
      var sqlText = ""
      Source.fromInputStream(is).getLines().foreach(text => {
        try {
          sql.executeSQL(text)
        } catch {
          case ignored: SQLException =>
        }
      })
      is.close()
    })
    sql.close()
    ryoServerAssist.getLogger.info("パッチの実行が完了しました。")
  }

}

package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

import scala.collection.mutable

class LoadNeoStackPage(ryoServerAssist: RyoServerAssist) {

  def loadStackPage(): Unit = {
    ryoServerAssist.getLogger.info("neoStackページをロード中...")
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackList")
    stackPageData = mutable.Map.empty
    while (rs.next()) {
      val category = rs.getString("category")
      val invItems = rs.getString("invItem")
      val page = rs.getInt("page")
      if (!stackPageData.contains(category)) {
        stackPageData += (category -> mutable.Map(page -> invItems))
      } else {
        stackPageData(category) += (page -> invItems)
      }
    }
    sql.close()
    ryoServerAssist.getLogger.info("neoStackページのロードが完了しました。")
  }

}

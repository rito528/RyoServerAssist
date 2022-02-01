package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.ItemList.itemList
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import .getLogger
import com.ryoserver.util.{Item, SQL}

import scala.collection.mutable

class LoadNeoStackPage() {

  def loadStackPage(): Unit = {
    getLogger.info("neoStackページをロード中...")
    val sql = new SQL()
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
      invItems.split(";").foreach(itemStack => {
        if (Item.getItemStackFromString(itemStack) != null) {
          itemList += Item.getOneItemStack(Item.getItemStackFromString(itemStack))
        }
      })
    }
    sql.close()
    getLogger.info("neoStackページのロードが完了しました。")
  }

}

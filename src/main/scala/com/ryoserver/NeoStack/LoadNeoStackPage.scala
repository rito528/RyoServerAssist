package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.ItemList.itemList
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

class LoadNeoStackPage(implicit ryoServerAssist: RyoServerAssist) {

  def loadStackPage(): Unit = {
    ryoServerAssist.getLogger.info("neoStackページをロード中...")
    implicit val session: AutoSession.type = AutoSession
    val stackListTable = sql"SELECT * FROM StackList"
    stackPageData = mutable.Map.empty
    if (stackListTable.getHeadData.nonEmpty) {
      case class StackData(category: String, invItems: String, page: Int)
      stackListTable.map(rs => {
        StackData(rs.string("category"), rs.string("invItem"), rs.int("page"))
      }).toIterable().apply().foreach(stackData => {
        if (!stackPageData.contains(stackData.category)) {
          stackPageData += (stackData.category -> mutable.Map(stackData.page -> stackData.invItems))
        } else {
          stackPageData(stackData.category) += (stackData.page -> stackData.invItems)
        }
        stackData.invItems.split(";").foreach(itemStackString => {
          val itemStack = Item.getItemStackFromString(itemStackString)
          if (itemStack != null) itemList += itemStack
        })
      })
    }
    ryoServerAssist.getLogger.info("neoStackページのロードが完了しました。")
  }

}

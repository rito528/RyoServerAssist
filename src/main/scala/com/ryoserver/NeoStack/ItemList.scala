package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ScalikeJDBC.getData
import com.ryoserver.util.{Item, SQL}
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

object ItemList {

  var stackList: mutable.Map[ItemStack, String] = mutable.Map.empty
  //Set型にすることで重複を許さない(重複するとアイテム数に齟齬が起きる可能性がある)
  var itemList: Set[ItemStack] = Set.empty

  def loadItemList(implicit ryoServerAssist: RyoServerAssist): Unit = {
    ryoServerAssist.getLogger.info("neoStackアイテムロード中...")
    implicit val session: AutoSession.type = AutoSession
    val stackListTable = sql"SELECT invItem,category FROM StackList;"
    stackListTable.foreach(rs => {
      val category = rs.string("category")
      rs.string("invItem").split(";").foreach(itemStackString => {
        val itemStack = Item.getItemStackFromString(itemStackString)
        if (itemStack != null) {
          stackList += (Item.getOneItemStack(itemStack) -> category)
        }
      })
    })
    ryoServerAssist.getLogger.info("neoStackアイテムのロードが完了しました。")
  }

}

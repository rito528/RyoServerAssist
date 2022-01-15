package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object ItemList {

  var stackList: mutable.Map[ItemStack, String] = mutable.Map.empty

  def loadItemList(ryoServerAssist: RyoServerAssist): Unit = {
    ryoServerAssist.getLogger.info("neoStackアイテムロード中...")
    val sql = new SQL()
    val rs = sql.executeQuery("SELECT invItem,category FROM StackList;")
    while (rs.next()) {
      val category = rs.getString("category")
      rs.getString("invItem").split(";").foreach(item => {
        val is = Item.getItemStackFromString(item)
        if (is != null) {
          val one = Item.getOneItemStack(is)
          stackList += (one -> category)
        }
      })
    }
    sql.close()
    ryoServerAssist.getLogger.info("neoStackアイテムのロードが完了しました。")
  }

}

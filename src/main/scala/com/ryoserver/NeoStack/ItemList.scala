package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object ItemList {

  var stackList:mutable.Map[ItemStack,String] = mutable.Map.empty

  def loadItemList(ryoServerAssist: RyoServerAssist): Unit = {
    ryoServerAssist.getLogger.info("neoStackアイテムロード中...")
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery("SELECT invItem,category FROM StackList;")
    while (rs.next()) {
      val config = new YamlConfiguration
      val category = rs.getString("category")
      rs.getString("invItem").split(";").foreach(item => {
        config.loadFromString(item)
        val is = config.getItemStack("i",null)
        if (is != null) {
          is.setAmount(1)
          stackList += (is -> category)
        }
      })
    }
    sql.close()
    ryoServerAssist.getLogger.info("neoStackアイテムのロードが完了しました。")
  }

}

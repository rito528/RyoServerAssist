package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

object ItemList {

  var stackList:Array[ItemStack] = Array.empty

  def loadItemList(ryoServerAssist: RyoServerAssist): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery("SELECT item FROM StackList;")
    while (rs.next()) {
      val config = new YamlConfiguration
      config.loadFromString(rs.getString("item"))
      stackList :+= config.getItemStack("i",null)
    }
    sql.close()
  }

}

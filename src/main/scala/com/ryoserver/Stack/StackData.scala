package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

class StackData(ryoServerAssist: RyoServerAssist) {

  def getSetItems(uuid:String,category:String): Array[ItemStack] = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'Stack';")
    if (!checkTable.next()) {
      sql.executeSQL("CREATE TABLE Stack(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,PRIMARY KEY(`id`))")
      sql.close()
      Array.empty
    } else {
      val rs = sql.executeQuery(s"SELECT item FROM StackList WHERE category='$category';")
      var items: Array[ItemStack] = Array.empty
      while (rs.next()) {
        val config:YamlConfiguration = new YamlConfiguration
        config.loadFromString(rs.getString("item"))
        items :+= config.getItemStack("i",null)
      }
      sql.close()
      items
    }
  }

  def addItemList(itemStack: ItemStack,category: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'StackList';")
    if (!checkTable.next()) sql.executeSQL("CREATE TABLE StackList(id INT AUTO_INCREMENT,category TEXT,item TEXT,PRIMARY KEY(`id`));")
    val config:YamlConfiguration = new YamlConfiguration
    itemStack.setAmount(1)
    config.set("i",itemStack)
    val checkItem = sql.executeQuery("SELECT item FROM StackList")
    var exist = false
    while (checkItem.next()) if (config.saveToString() == checkItem.getString("item")) exist = true
    if (!exist) sql.purseFolder(s"INSERT INTO StackList (category,item) VALUES ('$category',?)",config.saveToString())
    sql.close()
  }

}

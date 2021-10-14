package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class StackData(ryoServerAssist: RyoServerAssist) {

  def getSetItems(uuid:String,category:String): Array[ItemStack] = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'StackData';")
    if (!checkTable.next()) {
      sql.executeSQL("CREATE TABLE StackData(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,amount INT,PRIMARY KEY(`id`))")
    }
    val checkList = sql.executeQuery("SHOW TABLES LIKE 'StackList';")
    if (!checkList.next()) {
      sql.executeSQL("CREATE TABLE StackList(id INT AUTO_INCREMENT,category TEXT,item TEXT,PRIMARY KEY(`id`));")
    }
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

  def checkItemList(itemStack: ItemStack): Boolean = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'StackList';")
    if (!checkTable.next()) sql.executeSQL("CREATE TABLE StackList(id INT AUTO_INCREMENT,category TEXT,item TEXT,PRIMARY KEY(`id`));")
    val config:YamlConfiguration = new YamlConfiguration
    val is = itemStack.clone()
    is.setAmount(1)
    config.set("i",is)
    val checkIs = sql.executeQueryPurseFolder("SELECT item FROM StackList WHERE item=?",config.saveToString())
    var rs = false
    if (checkIs.next()) rs = true
    rs
  }

  def addStack(itemStack: ItemStack,p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val checkTable = sql.executeQuery("SHOW TABLES LIKE 'StackData';")
    if (!checkTable.next()) {
      sql.executeSQL("CREATE TABLE StackData(id INT AUTO_INCREMENT,UUID TEXT,category TEXT,item TEXT,amount INT,PRIMARY KEY(`id`))")
    }
    val is = itemStack.clone()
    is.setAmount(1)
    val config = new YamlConfiguration
    config.set("i",is)
    val playerData = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='${p.getUniqueId.toString}' AND item=?",config.saveToString())
    if (playerData.next()) sql.purseFolder(s"UPDATE StackData SET amount = amount + ${itemStack.getAmount} WHERE UUID='${p.getUniqueId.toString}' AND item=?",config.saveToString())
    else sql.purseFolder(s"INSERT INTO StackData (UUID,category,item,amount) VALUES ('${p.getUniqueId.toString}','${getCategory(config.saveToString())}',?,${itemStack.getAmount})",config.saveToString())
    sql.close()
  }

  def getCategory(is:String): String = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQueryPurseFolder("SELECT category FROM StackList WHERE item=?",is)
    if (rs.next()) return rs.getString("category")
    null
  }

  def removeItemList(itemStack: ItemStack): Unit = {
    val sql = new SQL(ryoServerAssist)
    val config = new YamlConfiguration
    config.set("i",itemStack)
    sql.purseFolder("DELETE FROM StackList WHERE item=?",config.saveToString())
    sql.purseFolder("DELETE FROM StackData WHERE item=?",config.saveToString())
    sql.close()
  }

  def addItemToPlayer(p:Player,is:ItemStack,amount:Int): Unit = {
    if (is == null || amount == 0) return
    val itemStack = is.clone()
    val sql = new SQL(ryoServerAssist)
    val config = new YamlConfiguration
    config.set("i",is)
    val getAmount = sql.executeQueryPurseFolder(s"SELECT amount FROM StackData WHERE UUID='${p.getUniqueId.toString}' AND item=?",config.saveToString())
    var realAmount = 0
    if (getAmount.next()) realAmount = getAmount.getInt("amount")
    if (realAmount >= amount) realAmount = amount
    itemStack.setAmount(realAmount)
    sql.purseFolder(s"UPDATE StackData SET amount=amount - $realAmount WHERE UUID='${p.getUniqueId.toString}' AND item=?",config.saveToString())
    if (itemStack.getAmount != 0) {
      var emptyCheck = false
      p.getInventory.getContents.foreach(item =>{
        if (item == null) emptyCheck = true
        else if ((item.getType == is.getType && itemStack.getAmount < is.getMaxStackSize)) emptyCheck = true
      })
      p.getInventory.addItem(itemStack)
    }
    sql.close()
    p.playSound(p.getLocation,Sound.UI_BUTTON_CLICK,1,1)
  }

  def toggleAutoStack(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (isAutoStackEnabled(p)) sql.executeSQL(s"UPDATE Players SET autoStack=false WHERE UUID='${p.getUniqueId.toString}'")
    else sql.executeSQL(s"UPDATE Players SET autoStack=true WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
  }

  def isAutoStackEnabled(p:Player): Boolean = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT autoStack FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var enabled = false
    if (rs.next()) enabled = rs.getBoolean("autoStack")
    sql.close()
    enabled
  }

}

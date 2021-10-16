package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.Stack.PlayerData.playerData
import com.ryoserver.util.SQL
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

class StackData(ryoServerAssist: RyoServerAssist) {

  def getSetItems(category:String): Array[ItemStack] = {
    val sql = new SQL(ryoServerAssist)
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
    val config:YamlConfiguration = new YamlConfiguration
    val is = itemStack.clone()
    is.setAmount(1)
    ItemList.stackList.contains(is)
  }

  def addStack(itemStack: ItemStack,p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val is = itemStack.clone()
    is.setAmount(1)
    val config = new YamlConfiguration
    config.set("i",is)
    val uuid = p.getUniqueId.toString
    if (PlayerData.playerData.contains(uuid) && PlayerData.playerData(uuid).contains(is)) {
      PlayerData.playerData(uuid) += (is -> (PlayerData.playerData(uuid)(is) + itemStack.getAmount))
    } else if (PlayerData.playerData.contains(uuid) && !PlayerData.playerData(uuid).contains(is)) {
      var amount = 0
      if (getItemAmount(getCategory(config.saveToString()),p).contains(is)) {
        amount = getItemAmount(getCategory(config.saveToString()),p)(is)
      }
      PlayerData.playerData(uuid) += (is -> (amount + itemStack.getAmount))
    } else {
      val getAmount = sql.executeQueryPurseFolder(s"SELECT amount FROM StackData WHERE UUID='${p.getUniqueId.toString}' AND item=?", config.saveToString())
      var playerHasAmount = 0
      if (getAmount.next()) playerHasAmount = getAmount.getInt("amount")
      PlayerData.playerData += (uuid -> mutable.Map(is -> (playerHasAmount + itemStack.getAmount)))
    }
    sql.close()
  }

  def getItemAmount(category:String,p: Player): mutable.Map[ItemStack,Int] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT amount,item FROM StackData WHERE category='$category' AND UUID='${p.getUniqueId.toString}'")
    val amount = mutable.Map.empty[ItemStack,Int]
    while (rs.next()) {
      val config = new YamlConfiguration
      config.loadFromString(rs.getString("item"))
      amount += (config.getItemStack("i",null) -> rs.getInt("amount"))
    }
    amount
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
    val uuid = p.getUniqueId.toString
    config.set("i",is)
    var selectAmount = amount
    var playerHasAmount = 0
    if (!(playerData.contains(p.getUniqueId.toString) && playerData(p.getUniqueId.toString).contains(is))) {
      val getAmount = sql.executeQueryPurseFolder(s"SELECT amount FROM StackData WHERE UUID='${p.getUniqueId.toString}' AND item=?", config.saveToString())
      if (getAmount.next()) playerHasAmount = getAmount.getInt("amount")
    } else {
      playerHasAmount = playerData(uuid)(is)
    }
    if (playerHasAmount <= amount) selectAmount = playerHasAmount
    itemStack.setAmount(selectAmount)
    if (!playerData.contains(p.getUniqueId.toString)) {
      playerData += (p.getUniqueId.toString -> mutable.Map(is -> (playerHasAmount - selectAmount)))
    } else {
      playerData(p.getUniqueId.toString) += (is -> (playerHasAmount - selectAmount))
    }
    if (itemStack.getAmount != 0 && p.getInventory.firstEmpty() != -1) {
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

package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.PlayerData.{changedData, playerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item.getStringFromItemStack
import com.ryoserver.util.SQL
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

class NeoStackData(ryoServerAssist: RyoServerAssist) {

  def getSetItems(category: String): Array[ItemStack] = {
    var items: Array[ItemStack] = Array.empty
    ItemList.stackList.foreach { case (is, categoryData) =>
      if (category.equalsIgnoreCase(categoryData)) items :+= is
    }
    items
  }

  def editItemList(category: String, page: Int, invContents: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackList WHERE page=$page AND category='$category';")
    if (rs.next()) sql.purseFolder(s"UPDATE StackList SET invItem=? WHERE category='$category' AND page=$page;", invContents)
    else sql.purseFolder(s"INSERT INTO StackList (category,page,invItem) VALUES ('$category',$page,?);", invContents)
    sql.close()
  }

  def checkItemList(itemStack: ItemStack): Boolean = {
    val is = itemStack.clone()
    is.setAmount(1)
    ItemList.stackList.contains(is)
  }

  def addStack(itemStack: ItemStack, p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val is = itemStack.clone()
    is.setAmount(1)
    val config = new YamlConfiguration
    config.set("i", is)
    val uuid = p.getUniqueId.toString
    if (PlayerData.playerData.contains(uuid) && PlayerData.playerData(uuid).contains(is)) {
      PlayerData.playerData(uuid) += (is -> (PlayerData.playerData(uuid)(is) + itemStack.getAmount))
    } else if (PlayerData.playerData.contains(uuid) && !PlayerData.playerData(uuid).contains(is)) {
      var amount = 0
      if (getItemAmount(getCategory(itemStack), p).contains(is)) {
        amount = getItemAmount(getCategory(itemStack), p)(is)
      }
      PlayerData.playerData(uuid) += (is -> (amount + itemStack.getAmount))
    } else {
      val getAmount = sql.executeQueryPurseFolder(s"SELECT amount FROM StackData WHERE UUID='${p.getUniqueId.toString}' AND item=?", config.saveToString())
      var playerHasAmount = 0
      if (getAmount.next()) playerHasAmount = getAmount.getInt("amount")
      PlayerData.playerData += (uuid -> mutable.Map(is -> (playerHasAmount + itemStack.getAmount)))
    }
    if (!PlayerData.changedData.contains(uuid)) {
      var changedList = Array.empty[ItemStack]
      changedList :+= is
      PlayerData.changedData += (uuid -> changedList)
    } else {
      PlayerData.changedData(uuid) :+= is
    }
    sql.close()
  }

  def getItemAmount(category: String, p: Player): mutable.Map[ItemStack, Int] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT amount,item FROM StackData WHERE category='$category' AND UUID='${p.getUniqueId.toString}'")
    val amount = mutable.Map.empty[ItemStack, Int]
    while (rs.next()) {
      val config = new YamlConfiguration
      config.loadFromString(rs.getString("item"))
      amount += (config.getItemStack("i", null) -> rs.getInt("amount"))
    }
    sql.close()
    amount
  }

  def getAllItemAmount(p: Player): mutable.Map[ItemStack, Int] = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT amount,item FROM StackData WHERE UUID='${p.getUniqueId.toString}'")
    val amount = mutable.Map.empty[ItemStack, Int]
    while (rs.next()) {
      val config = new YamlConfiguration
      config.loadFromString(rs.getString("item"))
      amount += (config.getItemStack("i", null) -> rs.getInt("amount"))
    }
    sql.close()
    amount
  }

  def removeNeoStack(p:Player,is:ItemStack,amount:Int): Int = {
    val itemStack = is.clone()
    itemStack.setAmount(1)
    val uuid = p.getUniqueId.toString
    if (!playerData(uuid).contains(itemStack)) return 0
    val hasAmount = playerData(uuid)(itemStack)
    playerData(uuid) = playerData(uuid).filterNot{case (oldIs,_) => oldIs == itemStack}
    if (amount > hasAmount) {
      playerData(uuid) += (itemStack -> hasAmount)
      addChangedData(p,itemStack)
      hasAmount
    } else {
      playerData(uuid) += (itemStack -> (hasAmount - amount))
      addChangedData(p,itemStack)
      amount
    }
  }

  private def addChangedData(p:Player,is:ItemStack): Unit = {
    val uuid = p.getUniqueId.toString
    if (!changedData.contains(uuid)) changedData += (uuid -> Array.empty[ItemStack])
    if (!changedData(uuid).contains(is)) changedData(uuid) :+= is
  }

  def getCategory(is: ItemStack): String = {
    val sql = new SQL(ryoServerAssist)
    val cloneIs = is.clone()
    cloneIs.setAmount(1)
    val config = new YamlConfiguration
    config.set("i", cloneIs)
    val rs = sql.executeQuery("SELECT category,invItem FROM StackList")
    while (rs.next()) {
      val category = rs.getString("category")
      val items = rs.getString("invItem").split(';')
      items.foreach(item => {
        val itemConfig = new YamlConfiguration
        itemConfig.loadFromString(item)
        val itemIs = itemConfig.getItemStack("i", null)
        if (itemIs != null) {
          itemIs.setAmount(1)
          if (is == itemIs && item != "") {
            sql.close()
            return category
          }
        }
      })
    }
    sql.close()
    null
  }

  def removeItemList(itemStack: ItemStack): Unit = {
    val sql = new SQL(ryoServerAssist)
    val config = new YamlConfiguration
    config.set("i", itemStack)
    sql.purseFolder("DELETE FROM StackList WHERE item=?", config.saveToString())
    sql.purseFolder("DELETE FROM StackData WHERE item=?", config.saveToString())
    sql.close()
  }

  def addItemToPlayer(p: Player, is: ItemStack, amount: Int): Unit = {
    if (is == null || amount == 0) return
    val itemStack = is.clone()
    val sql = new SQL(ryoServerAssist)
    val config = new YamlConfiguration
    val uuid = p.getUniqueId.toString
    config.set("i", is)
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
    if (!PlayerData.changedData.contains(uuid)) {
      var changedList = Array.empty[ItemStack]
      changedList :+= is
      PlayerData.changedData += (uuid -> changedList)
    } else {
      PlayerData.changedData(uuid) :+= is
    }
    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
  }

  def toggleAutoStack(p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (isAutoStackEnabled(p)) sql.executeSQL(s"UPDATE Players SET autoStack=false WHERE UUID='${p.getUniqueId.toString}'")
    else sql.executeSQL(s"UPDATE Players SET autoStack=true WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
  }

  def isAutoStackEnabled(p: Player): Boolean = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT autoStack FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var enabled = false
    if (rs.next()) enabled = rs.getBoolean("autoStack")
    sql.close()
    enabled
  }

}

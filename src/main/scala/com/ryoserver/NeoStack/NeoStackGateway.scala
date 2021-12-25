package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.PlayerData.changedData
import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NeoStackGateway(ryoServerAssist: RyoServerAssist) {

  def getPlayerHasNeoStackItems(p: Player): Map[ItemStack, Seq[Any]] = {
    val uuid = p.getUniqueId.toString
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackData WHERE UUID='$uuid';")
    var item: Map[ItemStack, Seq[Any]] = Map()
    while (rs.next()) {
      item = item ++ Map(Item.getItemStackFromString(rs.getString("item")) -> Seq(rs.getInt("amount"), rs.getString("category")))
    }
    sql.close()
    item
  }

  def editItemList(category: String, page: Int, invContents: String): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackList WHERE page=$page AND category='$category';")
    if (rs.next()) sql.purseFolder(s"UPDATE StackList SET invItem=? WHERE category='$category' AND page=$page;", invContents)
    else sql.purseFolder(s"INSERT INTO StackList (category,page,invItem) VALUES ('$category',$page,?);", invContents)
    sql.close()
  }

  def checkItemList(itemStack: ItemStack): Boolean = {
    ItemList.stackList.contains(Item.getOneItemStack(itemStack))
  }

  def addStack(itemStack: ItemStack, p: Player): Unit = {
    val oldPlayerData =
      PlayerData.playerData.filter(data => data.uuid == p.getUniqueId.toString && data.savingItemStack == Item.getOneItemStack(itemStack))
    if (oldPlayerData.isEmpty) {
      PlayerData.playerData :+= NeoStackDataType(p.getUniqueId.toString, Item.getOneItemStack(itemStack), null, itemStack.getAmount)
    } else {
      PlayerData.playerData =
        PlayerData.playerData.filterNot(data => data.uuid == p.getUniqueId.toString && data.savingItemStack == Item.getOneItemStack(itemStack))
      PlayerData.playerData :+=
        NeoStackDataType(oldPlayerData.head.uuid, oldPlayerData.head.savingItemStack, oldPlayerData.head.displayItemStack, oldPlayerData.head.amount + itemStack.getAmount)
    }
    addChangedData(p, Item.getOneItemStack(itemStack))
  }

  def getCategory(is: ItemStack): String = {
    NeoStackPageData.stackPageData.foreach { case (category, itemData) =>
      itemData.foreach { case (_, inv) => {
        inv.split(";").foreach(item => {
          if (Item.getOneItemStack(Item.getItemStackFromString(item)) == Item.getOneItemStack(is)) return category
        })
      }
      }
    }
    null
  }

  def removeNeoStack(p: Player, is: ItemStack, amount: Int): Int = {
    val playerData = PlayerData.playerData
      .filter(_.savingItemStack == Item.getOneItemStack(is))
      .filter(_.uuid == p.getUniqueId.toString)
    var minusAmount = 0
    if (playerData.nonEmpty) {
      if (playerData.head.amount >= amount) {
        minusAmount = amount
      } else {
        minusAmount = playerData.head.amount
      }
      PlayerData.playerData = PlayerData.playerData
        .filterNot {
          data => data.uuid == p.getUniqueId.toString && data.savingItemStack == Item.getOneItemStack(is)
        }
      PlayerData.playerData :+= NeoStackDataType(p.getUniqueId.toString, Item.getOneItemStack(is), null, playerData.head.amount - minusAmount)
    }
    if (minusAmount != 0) {
      addChangedData(p, is)
    }
    minusAmount
  }

  private def addChangedData(p: Player, is: ItemStack): Unit = {
    val uuid = p.getUniqueId.toString
    if (!changedData.contains(uuid)) changedData += (uuid -> Array.empty[ItemStack])
    if (!changedData(uuid).contains(is)) changedData(uuid) :+= is
  }

  def addItemToPlayer(p: Player, is: ItemStack, amount: Int): Unit = {
    if (is == null || amount == 0) return
    if (is.getAmount != 0 && p.getInventory.firstEmpty() != -1) {
      val playerData = PlayerData.playerData
        .filter(_.savingItemStack == is)
        .filter(_.uuid == p.getUniqueId.toString)
      val giveItem = is.clone()
      var minusAmount = 0
      if (playerData.nonEmpty) {
        if (playerData.head.amount >= amount) {
          minusAmount = amount
        } else {
          minusAmount = playerData.head.amount
        }
        PlayerData.playerData = PlayerData.playerData
          .filterNot {
            data => data.uuid == p.getUniqueId.toString && data.savingItemStack == is
          }
        PlayerData.playerData :+= NeoStackDataType(p.getUniqueId.toString, is, null, playerData.head.amount - minusAmount)
        giveItem.setAmount(minusAmount)
      }
      p.getInventory.addItem(giveItem)
      addChangedData(p, is)
    }
    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
  }

  def toggleAutoStack(p: Player): Unit = new RyoServerPlayer(p).toggleAutoStack()

  def isAutoStackEnabled(p: Player): Boolean = Data.playerData(p.getUniqueId).autoStack


}

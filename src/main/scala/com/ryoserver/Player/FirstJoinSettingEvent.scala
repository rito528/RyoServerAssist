package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class FirstJoinSettingEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "初参加アイテム設定画面") return
    val inv = e.getInventory
    val sql = new SQL(ryoServerAssist)
    var itemList = ""
    inv.getContents.foreach(is => {
      itemList += Item.getStringFromItemStack(is) + ";"
    })
    val checkRs = sql.executeQuery(s"SELECT * FROM firstJoinItems")
    if (checkRs.next()) sql.purseFolder(s"UPDATE firstJoinItems SET ItemStack=?", itemList)
    else sql.purseFolder(s"INSERT INTO firstJoinItems(ItemStack) VALUES (?);", itemList)
    sql.close()
  }

}

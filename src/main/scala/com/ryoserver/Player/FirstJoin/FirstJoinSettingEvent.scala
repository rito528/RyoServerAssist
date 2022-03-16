package com.ryoserver.Player.FirstJoin

import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class FirstJoinSettingEvent extends Listener {

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != "初参加アイテム設定画面") return
    implicit val session: AutoSession.type = AutoSession
    val inv = e.getInventory
    val itemList = inv.getContents.map(is => {
      Item.getStringFromItemStack(is) + ";"
    }).mkString("")
    val checkItemsTable = sql"SELECT * FROM firstJoinItems"
    if (checkItemsTable.getHeadData.nonEmpty) sql"UPDATE firstJoinItems SET ItemStack=$itemList".execute.apply()
    else sql"INSERT INTO firstJoinItems(ItemStack) VALUES ($itemList)".execute.apply()
  }

}

package com.ryoserver.Player.FirstJoin

import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.inventory.Inventory
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class FirstJoinGiveItemRepository extends TFirstJoinGiveItemRepository {

  private implicit val session: AutoSession.type = AutoSession

  override def store(inv: Inventory): Unit = {
    val itemList = inv.getContents.map(is => {
      Item.getStringFromItemStack(is) + ";"
    }).mkString("")
    val checkItemsTable = sql"SELECT * FROM firstJoinItems"
    if (checkItemsTable.getHeadData.nonEmpty) sql"UPDATE firstJoinItems SET ItemStack=$itemList".execute.apply()
    else sql"INSERT INTO firstJoinItems(ItemStack) VALUES ($itemList)".execute.apply()
  }

  override def restore(): Unit = {
    sql"SELECT ItemStack FROM firstJoinItems".foreach(rs =>{
      rs.string("ItemStack").split(';').map(Item.getItemStackFromString).foreach(itemStack => {
        FirstJoinGiveItemEntity.giveItems += itemStack
      })
    })
  }

}

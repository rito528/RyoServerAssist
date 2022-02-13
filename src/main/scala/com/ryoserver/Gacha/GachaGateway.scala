package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class GachaGateway {

  private implicit val session: AutoSession.type = AutoSession

  def addGachaItem(implicit ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Int): Unit = {
    sql"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,${Item.getStringFromItemStack(Item.getOneItemStack(is))})"
      .execute.apply()
  }

  def listGachaItem(rarity: Int, p: Player): Unit = {
    p.sendMessage("ガチャアイテムリスト")
    p.sendMessage("+--------------------------+")
    sql"SELECT * FROM GachaItems WHERE Rarity=$rarity".foreach(rs => {
      val itemStack = Item.getItemStackFromString(rs.string("Material"))
      p.sendMessage("ID:" + rs.int("id") + " アイテム名:" + (
        if (itemStack.getItemMeta.hasDisplayName) itemStack.getItemMeta.getDisplayName
        else itemStack.getType.name())
      )
    })
    p.sendMessage("+--------------------------+")
  }

  def removeGachaItem(id: Int): Unit = {
    sql"DELETE FROM GachaItems WHERE id=$id".execute.apply()
  }

}

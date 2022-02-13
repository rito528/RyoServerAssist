package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class GachaGateway {

  private implicit val session: AutoSession.type = AutoSession

  def addGachaItem(implicit ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Rarity): Unit = {
    val oneItemStack = Item.getOneItemStack(is)
    sql"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,${Item.getStringFromItemStack(oneItemStack)})"
      .execute.apply()
    GachaLoader.addGachaItemData(oneItemStack,rarity)
  }

  def removeGachaItem(id: Int,itemStack: ItemStack): Unit = {
    sql"DELETE FROM GachaItems WHERE id=$id".execute.apply()
    GachaLoader.removeGachaItem(itemStack)
  }

  def listGachaItem(rarity: Rarity): Map[Int,ItemStack] = {
    sql"SELECT * FROM GachaItems WHERE Rarity=${rarity.id}".map(rs =>{
      val itemStack = Item.getItemStackFromString(rs.string("Material"))
      val id = rs.int("id")
      id -> itemStack
    }).toList().apply().toMap
  }


}

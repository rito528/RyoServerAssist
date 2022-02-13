package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class GachaGateway {

  private implicit val session: AutoSession.type = AutoSession

  def addGachaItem(implicit ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Rarity): Unit = {
    sql"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,${Item.getStringFromItemStack(Item.getOneItemStack(is))})"
      .execute.apply()
  }

  def listGachaItem(rarity: Rarity): Map[Int,ItemStack] = {
    sql"SELECT * FROM GachaItems WHERE Rarity=${rarity.id}".map(rs =>{
      val itemStack = Item.getItemStackFromString(rs.string("Material"))
      val id = rs.int("id")
      id -> itemStack
    }).toList().apply().toMap
  }

  def removeGachaItem(id: Int): Unit = {
    sql"DELETE FROM GachaItems WHERE id=$id".execute.apply()
  }

}

package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

object GachaLoader {

  private implicit val session: AutoSession.type = AutoSession

  // 1 = miss, 2 = per,3 = bigPer, 4 = special
  lazy val perItemList: mutable.Set[ItemStack] = getGachaItems(1)
  lazy val bigPerItemList: mutable.Set[ItemStack] = getGachaItems(2)
  lazy val specialItemList: mutable.Set[ItemStack] = getGachaItems(3)
  lazy val missItemList: mutable.Set[ItemStack] = getGachaItems(4)

  private def getGachaItems(rarity: Int): mutable.Set[ItemStack] = {
    mutable.Set() ++ sql"SELECT * FROM GachaItems WHERE Rarity=$rarity"
      .map(rs => Item.getOneItemStack(Item.getItemStackFromString(rs.string("Material")))).toList().apply()
  }

  lazy val per: Double = getConfig.per //あたり
  lazy val bigPer: Double = getConfig.bigPer //大当たり
  lazy val special: Double = getConfig.Special //特等
  lazy val miss: Double = 100.0 - per - bigPer - special //ハズレ

  @deprecated //このクラスはあくまでloaderなのでaddItemがあるのはおかしい
  def addGachaItem(implicit ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Int): Unit = {
    sql"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,${Item.getStringFromItemStack(Item.getOneItemStack(is))})"
      .execute.apply()
  }

  @deprecated
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

  @deprecated
  def removeGachaItem(id: Int): Unit = {
    sql"DELETE FROM GachaItems WHERE id=$id".execute.apply()
  }
}

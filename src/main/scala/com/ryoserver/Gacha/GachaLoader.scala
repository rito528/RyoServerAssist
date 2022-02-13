package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.util.Item
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

object GachaLoader {

  private implicit val session: AutoSession.type = AutoSession

  // 1 = miss, 2 = per,3 = bigPer, 4 = special
  lazy val perItemList: mutable.Iterable[ItemStack] = getGachaItems(1)
  lazy val bigPerItemList: mutable.Iterable[ItemStack] = getGachaItems(2)
  lazy val specialItemList: mutable.Iterable[ItemStack] = getGachaItems(3)
  lazy val missItemList: mutable.Iterable[ItemStack] = getGachaItems(4)

  private def getGachaItems(rarity: Int): mutable.Iterable[ItemStack] = {
    mutable.Iterable() ++ sql"SELECT * FROM GachaItems WHERE Rarity=$rarity"
      .map(rs => Item.getOneItemStack(Item.getItemStackFromString(rs.string("Material")))).toIterable().apply()
  }

  lazy val per: Double = getConfig.per //あたり
  lazy val bigPer: Double = getConfig.bigPer //大当たり
  lazy val special: Double = getConfig.Special //特等
  lazy val miss: Double = 100.0 - per - bigPer - special //ハズレ

}

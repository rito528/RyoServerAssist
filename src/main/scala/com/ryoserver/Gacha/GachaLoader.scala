package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.util.Item
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

object GachaLoader {

  private implicit lazy val session: AutoSession.type = AutoSession

  /*
    アイテムデータ
    1 = miss, 2 = per,3 = bigPer, 4 = special
   */
  private lazy val gachaItemData: mutable.Map[ItemStack,Rarity] = mutable.Map() ++ sql"SELECT * FROM GachaItems"
    .map(rs => Item.getOneItemStack(Item.getItemStackFromString(rs.string("Material"))) ->
      Rarity.values.filter(_.id == rs.int("Rarity")).head)
    .toList()
    .apply()
    .toMap

  def getGachaItemData: mutable.Map[ItemStack,Rarity] = gachaItemData

  def addGachaItemData(itemStack: ItemStack,rarity: Rarity): Unit = gachaItemData += (itemStack -> rarity)

  def removeGachaItem(itemStack: ItemStack): Unit = gachaItemData -= itemStack

  /*
    ガチャの確率
   */
  lazy val per: Double = getConfig.per //あたり
  lazy val bigPer: Double = getConfig.bigPer //大当たり
  lazy val special: Double = getConfig.Special //特等
  lazy val miss: Double = 100.0 - per - bigPer - special //ハズレ

}

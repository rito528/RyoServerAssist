package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

object GachaLoader {

  // 1 = miss, 2 = per,3 = bigPer, 4 = special
  var perItemList: Array[ItemStack] = Array.empty
  var bigPerItemList: Array[ItemStack] = Array.empty
  var specialItemList: Array[ItemStack] = Array.empty
  var missItemList: Array[ItemStack] = Array.empty

  var per: Double = _ //あたり
  var bigPer: Double = _ //大当たり
  var special: Double = _ //特等

  def addGachaItem(implicit ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Int): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,${Item.getStringFromItemStack(Item.getOneItemStack(is))})"
      .execute.apply()
    unload(ryoServerAssist)
    load(ryoServerAssist)
  }

  def load(implicit ryoServerAssist: RyoServerAssist): Unit = {
    gachaItemLoad(ryoServerAssist)
    gachaRarityLoad(ryoServerAssist)
  }

  private def gachaItemLoad(implicit ryoServerAssist: RyoServerAssist): Unit = {
    ryoServerAssist.getLogger.info("ガチャアイテムロード中....")
    implicit val session: AutoSession.type = AutoSession
    val gachaItemTable = sql"SELECT * FROM GachaItems"
    gachaItemTable.foreach(rs => {
      val rarity = rs.int("Rarity")
      val itemStack = Item.getOneItemStack(Item.getItemStackFromString(rs.string("Material")))
      if (rarity == 0) missItemList :+= itemStack
      else if (rarity == 1) perItemList :+= itemStack
      else if (rarity == 2) bigPerItemList :+= itemStack
      else if (rarity == 3) specialItemList :+= itemStack
    })
    ryoServerAssist.getLogger.info("ガチャアイテムロードが完了しました！")
  }

  private def gachaRarityLoad(implicit ryoServerAssist: RyoServerAssist): Unit = {
    /*
      各レアリティの割合を計算
     */
    ryoServerAssist.getLogger.info("ガチャ排出割合読み込み中...")
    per = getConfig.per
    bigPer = getConfig.bigPer
    special = getConfig.Special
    val miss = 100.0 - per - bigPer - special
    if (miss <= 0) {
      ryoServerAssist.getLogger.warning("ガチャ割合の指定が不正です。")
      ryoServerAssist.getLogger.warning("サーバーを停止します。")
      Bukkit.shutdown()
    }
    ryoServerAssist.getLogger.info("ガチャ排出割合読み込みが完了しました！")
  }

  def unload(implicit ryoServerAssist: RyoServerAssist): Unit = {
    ryoServerAssist.getLogger.info("ガチャリストをアンロードしています...")
    perItemList = Array.empty
    bigPerItemList = Array.empty
    specialItemList = Array.empty
    missItemList = Array.empty
    ryoServerAssist.getLogger.info("ガチャリストをアンロードしました。")
  }

  def listGachaItem(rarity: Int, p: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
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
    implicit val session: AutoSession.type = AutoSession
    sql"DELETE FROM GachaItems WHERE id=$id".execute.apply()
  }
}

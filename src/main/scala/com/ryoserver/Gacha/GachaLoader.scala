package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Logger.getLogger
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GachaLoader {

  // 1 = miss, 2 = per,3 = bigPer, 4 = special
  var perItemList: Array[ItemStack] = Array.empty
  var bigPerItemList: Array[ItemStack] = Array.empty
  var specialItemList: Array[ItemStack] = Array.empty
  var missItemList: Array[ItemStack] = Array.empty

  var per: Double = _ //あたり
  var bigPer: Double = _ //大当たり
  var special: Double = _ //特等

  def addGachaItem(ryoServerAssist: RyoServerAssist, is: ItemStack, rarity: Int): Unit = {
    val sql = new SQL()
    val config: YamlConfiguration = new YamlConfiguration
    is.setAmount(1)
    config.set("i", is)
    sql.purseFolder(s"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,?);", config.saveToString())
    sql.close()
    unload(ryoServerAssist)
    load(ryoServerAssist)
  }

  def load(ryoServerAssist: RyoServerAssist): Unit = {
    gachaItemLoad()
    gachaRarityLoad(ryoServerAssist)
  }

  private def gachaItemLoad(): Unit = {
    Bukkit.getLogger.info("ガチャアイテムロード中....")
    val sql = new SQL()
    val rs = sql.executeQuery("SELECT * FROM GachaItems")
    while (rs.next()) {
      val rarity = rs.getInt("Rarity")
      val config = new YamlConfiguration
      config.loadFromString(rs.getString("Material"))
      if (rarity == 0) missItemList :+= config.getItemStack("i", null)
      else if (rarity == 1) perItemList :+= config.getItemStack("i", null)
      else if (rarity == 2) bigPerItemList :+= config.getItemStack("i", null)
      else if (rarity == 3) specialItemList :+= config.getItemStack("i", null)
    }
    Bukkit.getLogger.info("ガチャアイテムロードが完了しました！")
    sql.close()
  }

  private def gachaRarityLoad(ryoServerAssist: RyoServerAssist): Unit = {
    /*
      各レアリティの割合を計算
     */
    getLogger.info("ガチャ排出割合読み込み中...")
    per = getConfig.per
    bigPer = getConfig.bigPer
    special = getConfig.Special
    val miss = 100.0 - per - bigPer - special
    if (miss <= 0) {
      getLogger.warning("ガチャ割合の指定が不正です。")
      getLogger.warning("サーバーを停止します。")
      Bukkit.shutdown()
    }
    getLogger.info("ガチャ排出割合読み込みが完了しました！")
  }

  def unload(ryoServerAssist: RyoServerAssist): Unit = {
    getLogger.info("ガチャリストをアンロードしています...")
    perItemList = Array.empty
    bigPerItemList = Array.empty
    specialItemList = Array.empty
    missItemList = Array.empty
    getLogger.info("ガチャリストをアンロードしました。")
  }

  def listGachaItem(rarity: Int, p: Player): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT * FROM GachaItems WHERE Rarity=$rarity")
    p.sendMessage("ガチャアイテムリスト")
    p.sendMessage("+--------------------------+")
    while (rs.next()) {
      val config: YamlConfiguration = new YamlConfiguration
      config.loadFromString(rs.getString("Material"))
      p.sendMessage("ID:" + rs.getInt("id") + " アイテム名:" + (if (config.getItemStack("i", null).getItemMeta.hasDisplayName)
        config.getItemStack("i", null).getItemMeta.getDisplayName else config.getItemStack("i", null).getType.name()))
    }
    p.sendMessage("+--------------------------+")
    sql.close()
  }

  def removeGachaItem(id: Int): Unit = {
    val sql = new SQL()
    sql.executeSQL(s"DELETE FROM GachaItems WHERE id=$id;")
    sql.close()
  }
}

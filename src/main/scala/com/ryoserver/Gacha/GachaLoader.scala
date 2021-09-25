package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}
import org.bukkit.{Bukkit, Material}

import java.nio.file.{Files, Paths}
import scala.io.Source

object GachaLoader {

  // 1 = miss, 2 = per,3 = bigPer, 4 = special
  var perItemList:Array[String] = Array.empty
  var bigPerItemList:Array[String] = Array.empty
  var specialItemList:Array[String] = Array.empty
  var missItemList:Array[String] = Array.empty

  var per:Double = _ //あたり
  var bigPer:Double = _ //大当たり
  var special:Double = _ //特等

  def load(ryoServerAssist: RyoServerAssist): Unit = {
    gachaItemLoad(ryoServerAssist)
    gachaRarityLoad(ryoServerAssist)
  }

  def createGachaTable(ryoServerAssist: RyoServerAssist): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery("SHOW TABLES LIKE 'GachaItems'")
    if (!rs.next()) sql.executeSQL("CREATE TABLE GachaItems(id INT AUTO_INCREMENT,Rarity INT,Material TEXT,PRIMARY KEY(`id`));")
    sql.close()
  }

  def addGachaItem(ryoServerAssist: RyoServerAssist, is:ItemStack, rarity: Int): Unit = {
    createGachaTable(ryoServerAssist)
    val sql = new SQL(ryoServerAssist)
    var config:YamlConfiguration = null
    config = new YamlConfiguration
    config.set("i",is)
    sql.purseFolder(s"INSERT INTO GachaItems(Rarity,Material) VALUES ($rarity,?);",config.saveToString())
    sql.close()
  }

  def listGachaItem(ryoServerAssist: RyoServerAssist,rarity:Int,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM GachaItems WHERE Rarity=$rarity")
    p.sendMessage("ガチャアイテムリスト")
    p.sendMessage("+--------------------------+")
    while (rs.next()) {
      val config:YamlConfiguration = new YamlConfiguration
      config.loadFromString(rs.getString("Material"))
      p.sendMessage("ID:" + rs.getInt("id") + " アイテム名:" + (if (config.getItemStack("i",null).getItemMeta.hasDisplayName)
        config.getItemStack("i",null).getItemMeta.getDisplayName else config.getItemStack("i",null).getType.name()))
    }
    p.sendMessage("+--------------------------+")
    sql.close()
  }

  private def gachaItemLoad(ryoServerAssist: RyoServerAssist): Unit = {
    createGachaTable(ryoServerAssist)
    Bukkit.getLogger.info("ガチャアイテムロード中....")
    val gachaFile = Paths.get("plugins/RyoServerAssist/gachaItem.rsva")
    if (Files.notExists(gachaFile)) gachaFile.toFile.createNewFile()

      val gachaSource = Source.fromFile(gachaFile.toFile.getPath,"UTF-8")
      gachaSource.getLines().foreach(line =>{
        val material = line.split(",")(0)
        val rarity = line.split(",")(1).toInt
        if (Material.matchMaterial(material) == null) {
          Bukkit.getLogger.warning(material + "という不明なガチャアイテムが指定されています！")
          Bukkit.getLogger.warning("サーバーを停止します。")
          Bukkit.shutdown()
        } else if (!(rarity == 1 || rarity == 2 || rarity == 3 || rarity == 4)) {
          Bukkit.getLogger.warning(rarity + "という不明なレアリティが指定されています！")
          Bukkit.getLogger.warning("サーバーを停止します。")
          Bukkit.shutdown()
        } else {
          if (rarity == 1) missItemList :+= material
          else if (rarity == 2) perItemList :+= material
          else if (rarity == 3) bigPerItemList :+= material
          else if (rarity == 4) specialItemList :+= material
        }
      })
      Bukkit.getLogger.info("ガチャアイテムロードが完了しました！")
  }

  private def gachaRarityLoad(ryoServerAssist: RyoServerAssist): Unit = {
    /*
      各レアリティの割合を計算
     */
    ryoServerAssist.getLogger.info("ガチャ排出割合読み込み中...")
    per = ryoServerAssist.getConfig.getDouble("per") //あたり
    bigPer = ryoServerAssist.getConfig.getDouble("bigPer") //大当たり
    special = ryoServerAssist.getConfig.getDouble("Special") //特等
    val miss = 100.0 - per - bigPer - special
    if (miss <= 0) {
      ryoServerAssist.getLogger.warning("ガチャ割合の指定が不正です。")
      ryoServerAssist.getLogger.warning("サーバーを停止します。")
      Bukkit.shutdown()
    }
    ryoServerAssist.getLogger.info("ガチャ排出割合読み込みが完了しました！")
  }
}

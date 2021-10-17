package com.ryoserver.NeoStack

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.PlayerData.playerData
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.util.SQL
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}

import java.util
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class NeoStackGUI(ryoServerAssist: RyoServerAssist) {

  def loadStackPage(): Unit = {
    ryoServerAssist.getLogger.info("neoStackページをロード中...")
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackList")
    stackPageData = mutable.Map.empty
    while (rs.next()) {
      val category = rs.getString("category")
      val invItems = rs.getString("invItem")
      val page = rs.getInt("page")
      if (!stackPageData.contains(category)) {
        stackPageData += (category -> mutable.Map(page -> invItems))
      } else {
        stackPageData(category) += (page -> invItems)
      }
    }
    sql.close()
    ryoServerAssist.getLogger.info("neoStackページのロードが完了しました。")
  }

  def openStack(p:Player,page:Int,category:String,isEdit:Boolean): Unit = {
    val inv = Bukkit.createInventory(null,54,(if (isEdit) "[Edit]" else "") + "neoStack:" + page)
    val uuid = p.getUniqueId.toString
    var index = 0
    var invItems = ""
    val data = new NeoStackData(ryoServerAssist).getItemAmount(category,p)
    if (stackPageData.contains(category) && stackPageData(category).contains(page)) invItems = stackPageData(category)(page)
    invItems.split(";").foreach(item => {
      val config = new YamlConfiguration
      config.loadFromString(item)
      val is = config.getItemStack("i",null)
      if (is != null) {
        is.setAmount(1)
        val setItem = is.clone()
        val meta = is.getItemMeta
        var amount = 0
        if (playerData.contains(uuid) && playerData(uuid).contains(is)) {
          amount = playerData(uuid)(is)
        } else {
          if (data.contains(is)) amount = data(is)
        }
        meta.setLore(List(
          s"${BLUE}${BOLD}保有数:$UNDERLINE${amount}個",
          s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
        ).asJava)
        is.setItemMeta(meta)
        if (!ItemData.itemData.contains(p.getName)) ItemData.itemData += (p.getName -> mutable.Map(is -> setItem))
        else ItemData.itemData(p.getName) += (is -> setItem)
        if (!playerData.contains(p.getUniqueId.toString)) playerData += (p.getUniqueId.toString -> mutable.Map(setItem -> amount))
        else playerData(p.getUniqueId.toString) += (setItem -> amount)
        inv.setItem(index, is)
      }
      index += 1
    })
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}前のページに戻ります。",List(s"${GRAY}クリックで戻ります。").asJava))
    if (isEdit) inv.setItem(49,getItem(Material.CHEST,s"${AQUA}アイテムを追加します。",List(s"${GRAY}クリックで追加メニューを開きます。").asJava))
    inv.setItem(53,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}次のページに移動します。",List(s"${GRAY}クリックで移動します。").asJava))
    p.openInventory(inv)
  }

  def openCategorySelectGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,45,"neoStackカテゴリ選択")
    val lore:java.util.List[String] = new util.ArrayList[String]()
    lore.add(s"${AQUA}左クリックで開きます。")
    if (p.hasPermission("ryoserverassist.neoStack")) lore.add(s"${RED}右クリックで編集メニューを開きます。")
    inv.setItem(11,getItem(Material.GRASS_BLOCK,s"${GREEN}主要ブロック",lore))
    inv.setItem(13,getItem(Material.OAK_SAPLING,s"${AQUA}主要アイテム",lore))
    inv.setItem(15,getItem(Material.HONEY_BOTTLE,s"${YELLOW}ガチャアイテム",lore))
    inv.setItem(19,getItem(Material.DIAMOND_PICKAXE,s"${YELLOW}ツール系",lore))
    inv.setItem(21,getItem(Material.BREAD,s"${YELLOW}食料系",lore))
    inv.setItem(23,getItem(Material.REDSTONE,s"${YELLOW}レッドストーン系",lore))
    inv.setItem(25,getItem(Material.OAK_SAPLING,s"${YELLOW}植物系",lore))
    inv.setItem(36,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}メニューに戻ります。",List(s"${AQUA}クリックで戻ります。").asJava))
    inv.setItem(40,getItem(Material.HOPPER,s"${WHITE}自動収納を${if (new NeoStackData(ryoServerAssist).isAutoStackEnabled(p)) "off" else "on"}にします。",
      List(s"${AQUA}クリックで切り替えます。",
      s"${WHITE}現在の状態:${if (new NeoStackData(ryoServerAssist).isAutoStackEnabled(p)) s"${GREEN}${BOLD}${UNDERLINE}on" else s"${RED}${BOLD}${UNDERLINE}off"}").asJava))
    inv.setItem(44,getItem(Material.CHEST_MINECART,s"${GREEN}インベントリ内のアイテムをstackに収納します。",List("クリックで収納します。").asJava))
    p.openInventory(inv)
  }

  def openAddGUI(p:Player,page:Int,category:String): Unit = {
    val inv = Bukkit.createInventory(null,54,"neoStackアイテム追加メニュー:" + page)
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT * FROM StackList WHERE page=$page AND category='$category';")
    var invContents = ""
    if (rs.next()) invContents = rs.getString("invItem")
    var index = 0
    invContents.split(';').foreach(invContent => {
      val config = new YamlConfiguration
      config.loadFromString(invContent)
      if (invContent != null) {
        inv.setItem(index, config.getItemStack("i", null))
      }
      index += 1
    })
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}メニューに戻ります。",List(s"${AQUA}クリックで戻ります。").asJava))
    inv.setItem(49,getItem(Material.NETHER_STAR,"クリックでリストを保存します。",List("カテゴリ:" + getSelectedCategory(p) + "のリストを保存します。").asJava))
    inv.setItem(53,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}次のページに移動します",List(s"${AQUA}クリックで移動します。").asJava))
    p.openInventory(inv)
  }

}

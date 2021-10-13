package com.ryoserver.Stack

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.Stack.PlayerData.getSelectedCategory
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}
import org.bukkit.ChatColor._

import scala.jdk.CollectionConverters._

class StackGUI(ryoServerAssist: RyoServerAssist) {

  def openStack(p:Player,page:Int,category:String,isEdit:Boolean): Unit = {
    val data = new StackData(ryoServerAssist)
    val items = data.getSetItems(p.getUniqueId.toString,category)
    val maxPage = items.length / 45 + 1
    var selectPage = page
    if (page >= maxPage) selectPage = maxPage
    val inv = Bukkit.createInventory(null,54,(if (isEdit) "[Edit]" else "") + "stack:" + selectPage)
    var index = 0
    items.foreach(item => {
      if (selectPage * 45 >= index && (selectPage - 1) * 45 <= index) inv.setItem(index,item)
      index += 1
    })
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}前のページに戻ります。",List(s"${GRAY}クリックで戻ります。").asJava))
    if (isEdit) inv.setItem(49,getItem(Material.CHEST,s"${AQUA}アイテムを追加します。",List(s"${GRAY}クリックで追加メニューを開きます。").asJava))
    inv.setItem(53,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}次のページに移動します。",List(s"${GRAY}クリックで移動します。").asJava))
    p.openInventory(inv)
  }

  def openCategorySelectGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,27,"stackカテゴリ選択")
    inv.setItem(11,getItem(Material.GRASS_BLOCK,s"${GREEN}主要ブロック",List(
      s"${AQUA}左クリックで開きます。",
      if (p.hasPermission("ryoserverassist.stack")) s"${RED}右クリックで編集メニューを開きます。" else ""
    ).asJava))
    inv.setItem(13,getItem(Material.OAK_SAPLING,s"${AQUA}主要アイテム",List(
      s"${AQUA}左クリックで開きます。",
      if (p.hasPermission("ryoserverassist.stack")) s"${RED}右クリックで編集メニューを開きます。" else ""
    ).asJava))
    inv.setItem(15,getItem(Material.HONEY_BOTTLE,s"${YELLOW}ガチャアイテム",List(
      s"${AQUA}左クリックで開きます。",
      if (p.hasPermission("ryoserverassist.stack")) s"${RED}右クリックで編集メニューを開きます。" else ""
    ).asJava))
    p.openInventory(inv)
  }

  def openAddGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,"アイテム追加メニュー")
    inv.setItem(49,getItem(Material.NETHER_STAR,"クリックでアイテムを追加します。",List("カテゴリ:" + getSelectedCategory(p) + "にアイテムを追加します").asJava))
    p.openInventory(inv)
  }

}

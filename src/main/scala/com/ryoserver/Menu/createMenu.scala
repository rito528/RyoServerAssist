package com.ryoserver.Menu

import com.ryoserver.Gacha.GachaPaperData
import com.ryoserver.Inventory.Item
import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import org.bukkit.{Bukkit, Material}
import org.bukkit.inventory.Inventory

import scala.jdk.CollectionConverters._

object createMenu {

  def menu(): Inventory = {
    val inv = Bukkit.createInventory(null,27,"りょう鯖メニュー")
    inv.setItem(0,getItem(Material.SHULKER_BOX,"運営からのガチャ券を受け取ります。",List("クリックで受け取ります。").asJava))
    inv.setItem(2,getItem(Material.CHEST,"ストレージを開きます。",List("クリックで開きます。").asJava))
    inv.setItem(4,getItem(Material.BOOK,"クエストを選択します。",List("クリックで開けます。").asJava))
    inv.setItem(6,GachaPaperData.menu)
    inv
  }
}

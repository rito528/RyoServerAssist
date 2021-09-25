package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.{Bukkit, Material}
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class gachaAddItemInventory {

  def openAddInventory(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,"ガチャアイテム追加メニュー")
    inv.setItem(46,getItem(Material.EXPERIENCE_BOTTLE,"はずれにアイテムを追加する",
      List("クリックでインベントリに入っているアイテムを",
    "はずれアイテムに追加します。").asJava))
    inv.setItem(48,getItem(Material.PHANTOM_MEMBRANE,"あたりにアイテムを追加する",
      List("クリックでインベントリに入っているアイテムを",
        "あたりアイテムに追加します。").asJava))
    inv.setItem(50,getItem(Material.DIAMOND,"はずれにアイテムを追加する",
      List("クリックでインベントリに入っているアイテムを",
        "大当たりアイテムに追加します。").asJava))
    inv.setItem(52,getItem(Material.NETHERITE_INGOT,"特等にアイテムを追加する",
      List("クリックでインベントリに入っているアイテムを",
        "特等アイテムに追加します。").asJava))
    p.openInventory(inv)
  }

}

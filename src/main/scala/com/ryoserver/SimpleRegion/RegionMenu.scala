package com.ryoserver.SimpleRegion

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

class RegionMenu {

  def menu(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,9,"保護メニュー")
    inv.setItem(2,getItem(Material.WOODEN_AXE,"木の斧を取得します。",List(
      "保護方法",
      "木の斧で始点を左クリックします。",
      "終点を対角で右クリックします。",
      "その後、ダイヤの斧をクリックします。").asJava))
    inv.setItem(4,getItem(Material.DIAMOND_AXE,"保護をします。",List(
      "クリックで保護を開始します。",
      "結果がチャットに表示されます。"
    ).asJava))
    inv.setItem(6,getItem(Material.GOLDEN_AXE,"保護編集メニューを開きます。",List(
      "クリックで保護編集メニューを開きます。",
      "自分が管理者の保護範囲内にいる必要があります。"
    ).asJava))
    p.openInventory(inv)
  }

}

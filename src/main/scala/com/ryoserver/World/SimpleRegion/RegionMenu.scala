package com.ryoserver.World.SimpleRegion

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}
import org.bukkit.ChatColor._

import scala.jdk.CollectionConverters._

class RegionMenu {

  def menu(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,9,"保護メニュー")
    inv.setItem(2,getItem(Material.WOODEN_AXE,s"${GREEN}木の斧を取得します。",List(
      s"${GRAY}保護方法",
      s"${GRAY}木の斧で始点を左クリックします。",
      s"${GRAY}終点を対角で右クリックします。",
      s"${GRAY}その後、ダイヤの斧をクリックします。").asJava))
    inv.setItem(4,getItem(Material.DIAMOND_AXE,s"${GREEN}保護をします。",List(
      s"${GRAY}クリックで保護を開始します。",
      s"${GRAY}結果がチャットに表示されます。"
    ).asJava))
    inv.setItem(6,getItem(Material.GOLDEN_AXE,s"${GREEN}保護編集メニューを開きます。",List(
      s"${GRAY}クリックで保護編集メニューを開きます。",
      s"${GRAY}自分が管理者の保護範囲内にいる必要があります。"
    ).asJava))
    p.openInventory(inv)
  }

}

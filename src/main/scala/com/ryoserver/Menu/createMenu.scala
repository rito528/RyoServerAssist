package com.ryoserver.Menu

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.{Bukkit, Material}
import org.bukkit.inventory.Inventory

import scala.jdk.CollectionConverters._

object createMenu {

  def menu(): Inventory = {
    val inv = Bukkit.createInventory(null,27,"りょう鯖メニュー")
    inv.setItem(0,getItem(Material.SHULKER_BOX,"運営からのガチャ券を受け取ります。",List("クリックで受け取ります。").asJava))
    inv
  }
}

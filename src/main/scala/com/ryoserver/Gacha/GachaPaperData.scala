package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.{ChatColor, Material}
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

object GachaPaperData {

  val normal:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "手に持って右クリックで引けます"
    ).asJava
    getItem(Material.PAPER,"ガチャ券",Lore)
  }

}

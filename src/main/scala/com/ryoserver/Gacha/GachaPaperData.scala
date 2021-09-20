package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.getGachaItem
import org.bukkit.inventory.ItemStack
import org.bukkit.{ChatColor, Material}

import scala.jdk.CollectionConverters._

object GachaPaperData {

  val normal:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "手に持って右クリックで引けます"
    ).asJava
    getGachaItem(Material.PAPER,"ガチャ券",Lore)
  }

  val fromAdmin:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "手に持って右クリックで引けます",
      ChatColor.WHITE + "運営からのお詫びです"
    ).asJava
    getGachaItem(Material.PAPER,"ガチャ券",Lore)
  }

}

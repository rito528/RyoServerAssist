package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import org.bukkit.{ChatColor, Material}
import org.bukkit.inventory.{ItemFlag, ItemStack}

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

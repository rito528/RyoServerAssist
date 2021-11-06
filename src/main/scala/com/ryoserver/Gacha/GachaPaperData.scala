package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.getEnchantEffectItem
import org.bukkit.inventory.ItemStack
import org.bukkit.{ChatColor, Material}

import scala.jdk.CollectionConverters._

object GachaPaperData {

  val normal: ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "手に持って右クリックで引けます"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券", Lore)
  }

  val fromAdmin: ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "手に持って右クリックで引けます",
      ChatColor.WHITE + "運営からのお詫びです"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券", Lore)
  }

  val menu: ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "クリックで入手します。"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券を受け取ります。", Lore)
  }

}

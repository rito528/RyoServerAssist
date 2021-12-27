package com.ryoserver.Gacha

import com.ryoserver.util.Item.getEnchantEffectItem
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

object GachaPaperData {

  val normal: ItemStack = {
    val Lore = List(
      s"${WHITE}手に持って右クリックで引けます"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券", Lore)
  }

  val fromAdmin: ItemStack = {
    val Lore = List(
      s"${WHITE}手に持って右クリックで引けます",
      s"${WHITE}運営からのお詫びです"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券", Lore)
  }

  val menu: ItemStack = {
    val Lore = List(
      s"${WHITE}クリックで入手します。"
    ).asJava
    getEnchantEffectItem(Material.PAPER, "ガチャ券を受け取ります。", Lore)
  }

}

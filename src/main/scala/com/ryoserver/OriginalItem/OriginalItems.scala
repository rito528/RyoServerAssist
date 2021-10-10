package com.ryoserver.OriginalItem

import com.ryoserver.Inventory.Item.getGachaItem
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.{ChatColor, Material}
import org.bukkit.inventory.{ItemFlag, ItemStack}

import scala.jdk.CollectionConverters._

object OriginalItems {

  val tiguruinoyaiba:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "オフハンドに持つとダメージ+3します。"
    ).asJava
    getGachaItem(Material.IRON_SWORD,"血狂の刃",Lore)
  }

  val elementalPickaxe:ItemStack = {
    val itemStack:ItemStack = new ItemStack(Material.NETHERITE_PICKAXE)
    val itemMeta:ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName("エレメンタルピッケル")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DIG_SPEED,5,false)
    itemMeta.addEnchant(Enchantment.SILK_TOUCH,1,false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

}

package com.ryoserver.OriginalItem

import com.ryoserver.util.Item.getEnchantEffectItem
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.attribute.{Attribute, AttributeModifier}
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.{Damageable, ItemMeta}
import org.bukkit.inventory.{EquipmentSlot, ItemFlag, ItemStack}

import java.util.UUID
import scala.jdk.CollectionConverters._

object OriginalItems {

  val oretaEiyuNoKen: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.IRON_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}折れた英雄の剣")
    itemMeta.setLore(List(s"${WHITE}オフハンドに持つとトーテム効果が発動します。", s"${WHITE}耐久値分だけ使えます。").asJava)
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemMeta.asInstanceOf[Damageable].setDamage(240)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val yuusyanotate: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.SHIELD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}勇者の盾")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DURABILITY, 3, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemMeta.setLore(List(s"${GRAY}手に持つと耐性2のエフェクトが付きます。").asJava)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val tokutoukoukanken: ItemStack = {
    val Lore = List(
      s"${WHITE}公共施設で特等アイテムと交換できます。"
    ).asJava
    getEnchantEffectItem(Material.PAPER, s"${BLUE}${BOLD}特等交換券", Lore)
  }

}

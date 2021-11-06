package com.ryoserver.Inventory

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.{ItemFlag, ItemStack}
import org.bukkit.inventory.meta.{ItemMeta, SkullMeta}

import scala.jdk.CollectionConverters._

object Item {
  val getItem: (Material, String, java.util.List[String]) => ItemStack = (material: Material, name: String, lore: java.util.List[String]) => {
    val itemStack: ItemStack = new ItemStack(material)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(name)
    itemMeta.setLore(lore)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val getEnchantEffectItem: (Material, String, java.util.List[String]) => ItemStack = (material: Material, name: String, lore: java.util.List[String]) => {
    val itemStack: ItemStack = new ItemStack(material)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(name)
    itemMeta.setLore(lore)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
    itemStack
  }

  val getPlayerSkull: (Player, String, List[String]) => ItemStack = (p: Player, displayName: String, lore: List[String]) => {
    val is = new ItemStack(Material.PLAYER_HEAD)
    val meta = is.getItemMeta.asInstanceOf[SkullMeta]
    meta.setOwningPlayer(p)
    meta.setDisplayName(displayName)
    meta.setLore(lore.asJava)
    is.setItemMeta(meta)
    is
  }

}

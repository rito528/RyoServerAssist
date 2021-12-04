package com.ryoserver.util

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.{ItemMeta, SkullMeta}
import org.bukkit.inventory.{ItemFlag, ItemStack}

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

  def getStringFromItemStack(itemStack: ItemStack): String = {
    val config = new YamlConfiguration
    config.set("i",itemStack)
    config.saveToString()
  }

  def getItemStackFromString(str: String): ItemStack = {
    val config = new YamlConfiguration
    config.loadFromString(str)
    config.getItemStack("i",null)
  }

  def getOneItemStack(itemStack: ItemStack): ItemStack = {
    val is = itemStack.clone()
    is.setAmount(1)
    is
  }

}
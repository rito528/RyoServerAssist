package com.ryoserver.util

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.{Damageable, ItemMeta, SkullMeta}
import org.bukkit.inventory.{ItemFlag, ItemStack}
import org.bukkit.{Material, OfflinePlayer, Sound}

import java.security.SecureRandom
import scala.jdk.CollectionConverters._

object Item {
  val getItem: (Material, String, java.util.List[String]) => ItemStack = (material: Material, name: String, lore: java.util.List[String]) => {
    val itemStack: ItemStack = new ItemStack(material)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(name)
    itemMeta.setLore(lore)
    itemStack.setItemMeta(itemMeta)
    getOneItemStack(itemStack)
  }

  val getEnchantEffectItem: (Material, String, java.util.List[String]) => ItemStack = (material: Material, name: String, lore: java.util.List[String]) => {
    val itemStack: ItemStack = new ItemStack(material)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(name)
    itemMeta.setLore(lore)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
    getOneItemStack(itemStack)
  }

  val getPlayerSkull: (OfflinePlayer, String, List[String]) => ItemStack = (p: OfflinePlayer, displayName: String, lore: List[String]) => {
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
    config.set("i", itemStack)
    config.saveToString()
  }

  def getItemStackFromString(str: String): ItemStack = {
    val config = new YamlConfiguration
    config.loadFromString(str)
    config.getItemStack("i", null)
  }

  def getOneItemStack(itemStack: ItemStack): ItemStack = {
    val is = itemStack.clone()
    is.setAmount(1)
    is
  }

  def itemAddDamage(p: Player, item: ItemStack): Unit = {
    if (item != null && item.getType.getMaxDurability != 0) {
      val meta = item.getItemMeta
      val random = SecureRandom.getInstance("SHA1PRNG")
      val durabilityLevel = meta.getEnchantLevel(Enchantment.DURABILITY)
      val probability = 100 / (durabilityLevel + 1)
      if (random.nextInt(100) <= probability) {
        val itemDamage = meta.asInstanceOf[Damageable].getDamage
        if (itemDamage + 1 > item.getType.getMaxDurability) {
          p.getInventory.setItemInMainHand(null)
          p.playSound(p.getLocation, Sound.ENTITY_ITEM_BREAK, 1, 1)
        } else if (!item.getItemMeta.isUnbreakable) {
          meta.asInstanceOf[Damageable].setDamage(itemDamage + 1)
        }
      }
      item.setItemMeta(meta)
    }
  }

}
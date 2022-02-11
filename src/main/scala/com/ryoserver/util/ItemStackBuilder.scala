package com.ryoserver.util

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.{ItemFlag, ItemStack}

import scala.jdk.CollectionConverters._

case class ItemStackBuilder(material: Material,
                            title: String,
                            lore: List[String],
                            effect: Boolean) {

  def title(title: String): ItemStackBuilder = this.copy(title = title)

  def lore(lore: List[String]): ItemStackBuilder = this.copy(lore = lore)

  def setEffect(): ItemStackBuilder = this.copy(effect = true)

  def build(): ItemStack = {
    val itemStack = new ItemStack(material,1)
    if (effect) itemStack.addUnsafeEnchantment(Enchantment.DURABILITY,1)
    val meta = itemStack.getItemMeta
    if (title != null) meta.setDisplayName(title)
    if (lore != Nil) meta.setLore(lore.asJava)
    if (effect) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    if (meta != null) itemStack.setItemMeta(meta)
    itemStack
  }

}

object ItemStackBuilder {
  def getDefault(material: Material): ItemStackBuilder = ItemStackBuilder(material,null,Nil,effect = false)
}

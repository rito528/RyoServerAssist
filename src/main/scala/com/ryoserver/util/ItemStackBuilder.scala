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
    val meta = itemStack.getItemMeta
    meta.setDisplayName(title)
    meta.setLore(lore.asJava)
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemStack.setItemMeta(meta)
    if (effect) itemStack.addUnsafeEnchantment(Enchantment.DURABILITY,1)
    itemStack
  }

}

object ItemStackBuilder {
  def getDefault(material: Material): ItemStackBuilder = ItemStackBuilder(material,null,Nil,effect = false)
}

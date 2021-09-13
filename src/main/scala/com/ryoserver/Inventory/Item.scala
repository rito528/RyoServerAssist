package com.ryoserver.Inventory

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object Item {
  val getItem:(Material,String,java.util.List[String]) => ItemStack = (material: Material,name:String,lore: java.util.List[String]) => {
    val itemStack:ItemStack = new ItemStack(material)
    val itemMeta:ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(name)
    itemMeta.setLore(lore)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }
}

package com.ryoserver.NeoStack

import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object ItemData {

  var itemData:mutable.Map[String,mutable.Map[ItemStack,ItemStack]] = mutable.Map.empty

}

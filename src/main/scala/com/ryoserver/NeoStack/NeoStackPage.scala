package com.ryoserver.NeoStack

import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object NeoStackPage {

  val pageData: mutable.Map[RawNeoStackPageContext,List[ItemStack]] = mutable.Map.empty

}

package com.ryoserver.NeoStack.NeoStackPage

import com.ryoserver.NeoStack.RawNeoStackPageContext
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object NeoStackPageEntity {

  private[NeoStackPage] val pageData: mutable.Map[RawNeoStackPageContext,List[ItemStack]] = mutable.Map.empty

}

package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext
import org.bukkit.inventory.ItemStack

import java.util.UUID
import scala.collection.mutable

object NeoStackItemEntity {

  private[NeoStackItem] val neoStackItem: mutable.Map[UUID,Set[RawNeoStackItemAmountContext]] = mutable.Map.empty

  private[NeoStackItem] val changedNeoStackItemCache: mutable.Map[UUID,Set[ItemStack]] = mutable.Map.empty

}

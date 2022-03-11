package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext
import org.bukkit.inventory.ItemStack

import java.util.UUID

trait TNeoStackItemRepository {

  def store(): Unit

  def restore(uuid: UUID): Unit

  def getItemList(uuid: UUID): Set[RawNeoStackItemAmountContext]

  def changeAmount(uuid: UUID,rawNeoStackItemAmountContext: RawNeoStackItemAmountContext): Boolean

  def getItemAmountContext(uuid: UUID,itemStack: ItemStack): Option[RawNeoStackItemAmountContext]

}

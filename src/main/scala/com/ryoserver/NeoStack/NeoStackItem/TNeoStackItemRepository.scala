package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext

import java.util.UUID

trait TNeoStackItemRepository {

  def store(): Unit

  def restore(uuid: UUID): Unit

  def getItemList(uuid: UUID): Set[RawNeoStackItemAmountContext]

}

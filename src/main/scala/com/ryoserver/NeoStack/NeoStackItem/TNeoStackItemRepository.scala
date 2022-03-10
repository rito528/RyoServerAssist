package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext

import java.util.UUID

trait TNeoStackItemRepository {

  def store(): Unit

  def restore(): Unit

  def getItemListBy(uuid: UUID): Set[RawNeoStackItemAmountContext]

}

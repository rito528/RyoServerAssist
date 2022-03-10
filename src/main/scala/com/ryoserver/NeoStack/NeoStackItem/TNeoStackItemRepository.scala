package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext

import java.util.UUID

trait TNeoStackItemRepository {

  val uuid: UUID

  def store(): Unit

  def restore(): Unit

  def getItemList: Set[RawNeoStackItemAmountContext]

}

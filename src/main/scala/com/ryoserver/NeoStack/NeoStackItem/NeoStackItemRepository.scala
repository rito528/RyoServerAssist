package com.ryoserver.NeoStack.NeoStackItem
import com.ryoserver.NeoStack.RawNeoStackItemAmountContext

import java.util.UUID

class NeoStackItemRepository extends TNeoStackItemRepository {

  override def store(): Unit = {

  }

  override def restore(): Unit = {

  }

  override def getItemListBy(uuid: UUID): Set[RawNeoStackItemAmountContext] = {
    NeoStackItemEntity.neoStackItem(uuid)
  }

}

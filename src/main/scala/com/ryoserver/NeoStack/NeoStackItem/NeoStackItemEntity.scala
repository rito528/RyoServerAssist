package com.ryoserver.NeoStack.NeoStackItem

import com.ryoserver.NeoStack.RawNeoStackItemAmountContext

import java.util.UUID
import scala.collection.mutable

object NeoStackItemEntity {

  private[NeoStackItem] val neoStackItem: mutable.Map[UUID,Set[RawNeoStackItemAmountContext]] = mutable.Map.empty

}

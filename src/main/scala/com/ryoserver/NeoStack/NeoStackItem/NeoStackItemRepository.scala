package com.ryoserver.NeoStack.NeoStackItem
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.NeoStack.RawNeoStackItemAmountContext
import com.ryoserver.util.Item
import scalikejdbc.{AutoSession, DB, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID

class NeoStackItemRepository(override val uuid: UUID) extends TNeoStackItemRepository {

  private implicit val session: AutoSession.type = AutoSession

  override def store(): Unit = {
    NeoStackItemEntity.neoStackItem.foreach{case (uuid,rawNeoStackItemAmountContexts) =>
      rawNeoStackItemAmountContexts.foreach(rawNeoStackItemAmountContext => {
        DB.localTx(session => {
          val itemStackString = Item.getStringFromItemStack(rawNeoStackItemAmountContext.itemStack)
          val amount = rawNeoStackItemAmountContext.amount
          sql""
        })
      })
    }
  }

  override def restore(): Unit = {
    val playerHasItemContext = sql"SELECT item,amount FROM StackData WHERE UUID=$uuid".map(rs => {
      val itemStack = Item.getOneItemStack(Item.getItemStackFromString(rs.string("item")))
      val amount = rs.int("amount")
      RawNeoStackItemAmountContext(itemStack,amount)
    }).toList().apply().toSet

    val playerHasItems = playerHasItemContext.map(_.itemStack)
    val neoStackPageRepository = new NeoStackPageRepository
    val playerNotHasItems = neoStackPageRepository.getAllItems.diff(playerHasItems).map(RawNeoStackItemAmountContext(_,0))

    val neoStackItemData = playerHasItemContext ++ playerNotHasItems
    NeoStackItemEntity.neoStackItem += uuid -> neoStackItemData
  }

  override def getItemList: Set[RawNeoStackItemAmountContext] = {
    NeoStackItemEntity.neoStackItem(uuid)
  }

}

package com.ryoserver.NeoStack.NeoStackItem
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.NeoStack.RawNeoStackItemAmountContext
import com.ryoserver.util.Item
import scalikejdbc.{AutoSession, DB, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID

class NeoStackItemRepository extends TNeoStackItemRepository {

  override def store(): Unit = {
    NeoStackItemEntity.neoStackItem.foreach{case (uuid,rawNeoStackItemAmountContexts) =>
      rawNeoStackItemAmountContexts.foreach(rawNeoStackItemAmountContext => {
        DB.localTx(_ => {
          val itemStackString = Item.getStringFromItemStack(rawNeoStackItemAmountContext.itemStack)
          val amount = rawNeoStackItemAmountContext.amount
          sql"""INSERT INTO StackData (UUID,item,amount) VALUES($uuid,$itemStackString,$amount)
               ON DUPLICATE KEY UPDATE
               item=$itemStackString,
               amount=$amount"""
            .execute()
        })
      })
    }
  }

  override def restore(uuid: UUID): Unit = {
    implicit val session: AutoSession.type = AutoSession
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

  override def getItemList(uuid: UUID): Set[RawNeoStackItemAmountContext] = {
    NeoStackItemEntity.neoStackItem(uuid)
  }

}

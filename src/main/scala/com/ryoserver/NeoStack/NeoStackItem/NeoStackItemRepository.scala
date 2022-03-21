package com.ryoserver.NeoStack.NeoStackItem
import com.ryoserver.NeoStack.NeoStackItem.NeoStackItemEntity.changedNeoStackItemCache
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.NeoStack.RawNeoStackItemAmountContext
import com.ryoserver.util.Item
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, DB, DBSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID
import scala.jdk.CollectionConverters.CollectionHasAsScala

class NeoStackItemRepository extends TNeoStackItemRepository {

  override def store(): Unit = {
    NeoStackItemEntity.neoStackItem.foreach{case (uuid,rawNeoStackItemAmountContexts) =>
      if (changedNeoStackItemCache.contains(uuid)) {
        rawNeoStackItemAmountContexts.filter(context => changedNeoStackItemCache(uuid).contains(context.itemStack)).foreach(rawNeoStackItemAmountContext => {
          DB.localTx(session => {
            implicit val iSession: DBSession = session
            val itemStackString = Item.getStringFromItemStack(rawNeoStackItemAmountContext.itemStack)
            val amount = rawNeoStackItemAmountContext.amount
            println(amount)
            sql"""INSERT INTO StackData (UUID,item,amount) VALUES (${uuid.toString},$itemStackString,$amount)
               ON DUPLICATE KEY UPDATE
               item=$itemStackString,
               amount=$amount"""
              .execute()
              .apply()
          })
        })
      }
    }
  }

  override def restore(uuid: UUID): Unit = {
    if (NeoStackItemEntity.neoStackItem.contains(uuid)) return
    implicit val session: AutoSession.type = AutoSession
    val playerHasItemContext = sql"SELECT item,amount FROM StackData WHERE UUID=${uuid.toString}".map(rs => {
      val itemStack = Item.getOneItemStack(Item.getItemStackFromString(rs.string("item")))
      val amount = rs.int("amount")
      RawNeoStackItemAmountContext(itemStack,amount)
    }).toList.apply.toSet

    val playerHasItems = playerHasItemContext.map(_.itemStack)
    val neoStackPageRepository = new NeoStackPageRepository
    val playerNotHasItems = neoStackPageRepository.getAllItems.diff(playerHasItems).filterNot(_ == null).map(data => RawNeoStackItemAmountContext(Item.getOneItemStack(data),0))
    val neoStackItemData = playerHasItemContext ++ playerNotHasItems
    NeoStackItemEntity.neoStackItem += uuid -> neoStackItemData
  }

  override def getItemList(uuid: UUID): Set[RawNeoStackItemAmountContext] = {
    NeoStackItemEntity.neoStackItem(uuid)
  }

  override def changeAmount(uuid: UUID,rawNeoStackItemAmountContext: RawNeoStackItemAmountContext): Boolean = {
    if (!isItemExists(rawNeoStackItemAmountContext.itemStack) || rawNeoStackItemAmountContext.amount < 0) return false
    NeoStackItemEntity.neoStackItem += uuid -> (getItemList(uuid).filterNot(_.itemStack == rawNeoStackItemAmountContext.itemStack) ++ Set(rawNeoStackItemAmountContext))
    changedNeoStackItemCache += uuid -> ((if (changedNeoStackItemCache.contains(uuid)) changedNeoStackItemCache(uuid) else Set.empty) ++ Set(rawNeoStackItemAmountContext.itemStack))
    println(changedNeoStackItemCache)
    true
  }

  override def getItemAmountContext(uuid: UUID, itemStack: ItemStack): Option[RawNeoStackItemAmountContext] = {
    if (!isItemExists(itemStack)) return None
    Option(getItemList(uuid).filter(_.itemStack == itemStack).head)
  }

  private def isItemExists(itemStack: ItemStack): Boolean = {
    val pageRepository = new NeoStackPageRepository
    pageRepository.getAllItems.contains(itemStack)
  }

}

package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.NeoStackItem.{NeoStackItemRepository, TNeoStackItemRepository}
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

class NeoStackService {

  private val neoStackItemRepository: TNeoStackItemRepository = new NeoStackItemRepository

  def autoStoreItem(implicit ryoServerAssist: RyoServerAssist): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        neoStackItemRepository.store()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
  }

  private def changeItemAmount(uuid: UUID,rawNeoStackItemAmountContext: RawNeoStackItemAmountContext): Boolean = {
    neoStackItemRepository.changeAmount(uuid, rawNeoStackItemAmountContext)
  }

  def getItemAmount(uuid: UUID,itemStack: ItemStack): Option[Int] = {
    val oneItemStack = Item.getOneItemStack(itemStack)
    for {
      amountContext <- neoStackItemRepository.getItemAmountContext(uuid,oneItemStack)
    } yield amountContext.amount
  }

  def addItemAmount(uuid: UUID,itemStack: ItemStack,addAmount: Int): Unit = {
    val oneItemStack = Item.getOneItemStack(itemStack)
    changeItemAmount(uuid,RawNeoStackItemAmountContext(oneItemStack,getItemAmount(uuid,oneItemStack).getOrElse(0) + addAmount))
  }

  def removeItemAmount(uuid: UUID,itemStack: ItemStack,removeAmount: Int): Option[Int] = {
    val amount = getItemAmount(uuid, Item.getOneItemStack(itemStack)).getOrElse(0)
    val removedAmount = if (amount >= removeAmount && changeItemAmount(uuid,RawNeoStackItemAmountContext(itemStack,amount - removeAmount))) {
      removeAmount
    } else {
      changeItemAmount(uuid,RawNeoStackItemAmountContext(itemStack,0))
      amount
    }
    Option(removedAmount)
  }

  def addItemToPlayer(p: Player,itemStack: ItemStack,takeAmount: Int): Unit = {
    val uuid = p.getUniqueId
    val oneItemStack = Item.getOneItemStack(itemStack)
    val giveItemStack = itemStack
    if (getItemAmount(uuid,oneItemStack).getOrElse(0) <= 0 && p.getInventory.firstEmpty() != -1) return
    giveItemStack.setAmount(removeItemAmount(uuid,oneItemStack,takeAmount).getOrElse(0))
    p.getInventory.addItem(giveItemStack)
    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
  }

  def isItemExists(itemStack: ItemStack): Boolean = {
    new NeoStackPageRepository().getAllItems.contains(Item.getOneItemStack(itemStack))
  }

}

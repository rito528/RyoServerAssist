package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.NeoStackItem.{NeoStackItemRepository, TNeoStackItemRepository}
import com.ryoserver.NeoStack.NeoStackPage.{NeoStackPageRepository, TNeoStackPageRepository}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

class NeoStackService {

  private val neoStackItemRepository: TNeoStackItemRepository = new NeoStackItemRepository
  private val neoStackPageRepository: TNeoStackPageRepository = new NeoStackPageRepository

  def autoStoreItem(implicit ryoServerAssist: RyoServerAssist): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        neoStackItemRepository.store()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
  }

  def changeGUIPlacement(category: Category,page: Int,inventoryContents: List[ItemStack]): Unit = {
    neoStackPageRepository.changeItem(category,page,inventoryContents)
  }

  def changeItemAmount(uuid: UUID,rawNeoStackItemAmountContext: RawNeoStackItemAmountContext): Boolean = {
    neoStackItemRepository.changeAmount(uuid, rawNeoStackItemAmountContext)
  }

  def addItemToPlayer(p: Player,itemStack: ItemStack,takeAmount: Int): Unit = {
    val uuid = p.getUniqueId
    val oneItemStack = Item.getOneItemStack(itemStack)
    for {
      amountContext <- neoStackItemRepository.getItemAmountContext(uuid,oneItemStack)
    } yield {
      if (!changeItemAmount(uuid,RawNeoStackItemAmountContext(oneItemStack,amountContext.amount - takeAmount))) return
      val giveItemStack = itemStack
      giveItemStack.setAmount(takeAmount)
      p.getInventory.addItem(giveItemStack)
      p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
    }
  }

}

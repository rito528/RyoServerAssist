package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.NeoStackItem.{NeoStackItemRepository, TNeoStackItemRepository}
import com.ryoserver.NeoStack.NeoStackPage.{NeoStackPageRepository, TNeoStackPageRepository}
import com.ryoserver.RyoServerAssist
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

}

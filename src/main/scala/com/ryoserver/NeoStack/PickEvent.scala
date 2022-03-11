package com.ryoserver.NeoStack

import com.ryoserver.NeoStack.NeoStackItem.NeoStackItemRepository
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.scheduler.BukkitRunnable

class PickEvent(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def playerPickEvent(e: EntityPickupItemEvent): Unit = {
    e.getEntity match {
      case p: Player =>
        if (!p.isAutoStack) return
        val pickupItemStack = e.getItem.getItemStack
        val amount = pickupItemStack.getAmount
        pickupItemStack.setAmount(1)
        val neoStackItemRepository = new NeoStackItemRepository
        if (!neoStackItemRepository.changeAmount(p.getUniqueId,RawNeoStackItemAmountContext(pickupItemStack,amount))) return
        e.setCancelled(true)
        e.getItem.remove()
        new BukkitRunnable {
          override def run(): Unit = {
            p.playSound(p.getLocation, Sound.ENTITY_ITEM_PICKUP, 1, 1)
          }
        }.runTaskLater(ryoServerAssist, 5)
      case _ =>
    }
  }


}

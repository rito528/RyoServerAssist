package com.ryoserver.NeoStack

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.scheduler.BukkitRunnable

class PickEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def playerPickEvent(e: EntityPickupItemEvent): Unit = {
    if (!e.getEntity.isInstanceOf[Player]) return
    val itemStack = e.getItem.getItemStack
    val data = new NeoStackGateway()
    val p = e.getEntity.asInstanceOf[Player]
    if (!data.checkItemList(itemStack) || !p.isAutoStack) return
    e.setCancelled(true)
    e.getItem.remove()
    new BukkitRunnable {
      override def run(): Unit = {
        data.addStack(itemStack, p)
        p.playSound(p.getLocation, Sound.ENTITY_ITEM_PICKUP, 1, 1)
      }
    }.runTaskLater(ryoServerAssist, 5)
  }


}

package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.scheduler.BukkitRunnable

class PickEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def playerPickEvent(e:EntityPickupItemEvent): Unit = {
    if (!e.getEntity.isInstanceOf[Player]) return
    val itemStack = e.getItem.getItemStack
    val data = new StackData(ryoServerAssist)
    val p = e.getEntity.asInstanceOf[Player]
    if (!data.checkItemList(itemStack) || !data.isAutoStackEnabled(p)) return
    data.addStack(itemStack,p)
    new BukkitRunnable {
      override def run(): Unit = {
        p.getInventory.removeItem(itemStack)
      }
    }.runTaskLater(ryoServerAssist,5)
  }



}

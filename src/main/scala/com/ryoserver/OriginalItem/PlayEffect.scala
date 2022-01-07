package com.ryoserver.OriginalItem

import com.ryoserver.RyoServerAssist
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

class PlayEffect(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def holdEvent(e: PlayerItemHeldEvent): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        val p = e.getPlayer
        if (p.getInventory.getItemInOffHand == OriginalItems.yuusyanotate || p.getInventory.getItemInMainHand == OriginalItems.yuusyanotate) {
          new BukkitRunnable {
            override def run(): Unit = {
              p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20,1))
            }
          }.runTask(ryoServerAssist)
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,10)
  }

}

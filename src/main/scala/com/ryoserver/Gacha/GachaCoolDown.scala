package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

object GachaCoolDown {

  /*
  ガチャを引くごとにクールタイムを設けないと2回同時に引いたりしてしまう
 */
  def pullCoolDownSet(p: Player, ryoServerAssist: RyoServerAssist): Unit = {
    setPullCoolDown(flag = true, p, ryoServerAssist)
    new BukkitRunnable {
      override def run(): Unit = {
        setPullCoolDown(flag = false, p, ryoServerAssist)
      }
    }.runTaskLater(ryoServerAssist, 5)
  }

  def setPullCoolDown(flag: Boolean, p: Player, ryoServerAssist: RyoServerAssist): Unit = {
    p.setMetadata("GachaCoolDown", new FixedMetadataValue(ryoServerAssist, flag))
  }

  def getCoolDown(p: Player): Boolean = {
    try {
      p.getMetadata("GachaCoolDown").get(0).value().asInstanceOf[Boolean]
    } catch {
      case _: Exception => false
    }
  }


}

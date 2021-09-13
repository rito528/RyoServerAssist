package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Sound}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class Gacha(ryoServerAssist: RyoServerAssist) extends Listener {
  ryoServerAssist.getServer.getPluginManager.registerEvents(this,ryoServerAssist)

  @EventHandler(priority=EventPriority.HIGHEST)
  def onPull(e:PlayerInteractEvent): Unit = {
    val p = e.getPlayer
    val inventory = p.getInventory
    val mainHand = inventory.getItemInMainHand
    val getItemAmount = inventory.getItemInMainHand.getAmount

    if (mainHand.getData == GachaPaperData.normal.getData && mainHand.getItemMeta == GachaPaperData.normal.getItemMeta && p.isSneaking && !getCoolDown(p)) {
      /*
        シフトと右クリックを同時に行った場合(全部引く場合)
      */
      inventory.getItemInMainHand.setAmount(0)
      p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      p.sendMessage(ChatColor.AQUA + getItemAmount.toString + "回ガチャを引きました！")
      pullCoolDownSet(p)
    } else if (mainHand.getData == GachaPaperData.normal.getData && mainHand.getItemMeta == GachaPaperData.normal.getItemMeta && !getCoolDown(p)) {
      /*
        1回だけ引く場合
      */
      inventory.getItemInMainHand.setAmount(getItemAmount - 1)
      p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      p.sendMessage(ChatColor.AQUA + "ガチャを引きました！")
      pullCoolDownSet(p)
    }
  }

  /*
    ガチャを引くごとにクールタイムを設けないと2回同時に引いたりしてしまう
   */
  def pullCoolDownSet(p:Player): Unit = {
    setPullCoolDown(flag = true,p)
    new BukkitRunnable {
      override def run(): Unit = {
        setPullCoolDown(flag = false,p)
      }
    }.runTaskLater(ryoServerAssist,5)
  }

  def setPullCoolDown(flag: Boolean,p:Player): Unit = {
    p.setMetadata("GachaCoolDown",new FixedMetadataValue(ryoServerAssist,flag))
  }

  def getCoolDown(p: Player): Boolean = {
    try {
      p.getMetadata("GachaCoolDown").get(0).value().asInstanceOf[Boolean]
    } catch {
      case e: Exception => false
    }
  }

}

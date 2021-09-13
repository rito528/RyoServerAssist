package com.ryoserver.Gacha

import com.ryoserver.Gacha.GachaCoolDown.{getCoolDown, pullCoolDownSet}
import com.ryoserver.RyoServerAssist
import org.bukkit.{ChatColor, Sound}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

class Gacha(ryoServerAssist: RyoServerAssist) extends Listener {

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
      pullCoolDownSet(p,ryoServerAssist)
    } else if (mainHand.getData == GachaPaperData.normal.getData && mainHand.getItemMeta == GachaPaperData.normal.getItemMeta && !getCoolDown(p)) {
      /*
        1回だけ引く場合
      */
      inventory.getItemInMainHand.setAmount(getItemAmount - 1)
      p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      p.sendMessage(ChatColor.AQUA + "ガチャを引きました！")
      pullCoolDownSet(p,ryoServerAssist)
    }
  }


}

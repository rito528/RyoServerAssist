package com.ryoserver.Gacha

import com.ryoserver.Gacha.GachaCoolDown.{getCoolDown, pullCoolDownSet}
import com.ryoserver.Gacha.GachaPaperData.enableGachaTickets
import com.ryoserver.OriginalItem.OriginalItems
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

class GachaHandler(implicit ryoServerAssist: RyoServerAssist) extends Listener {

  /*
    ガチャを引いた回数が1000 or 3000になったときに処理を行い、行った場合はtrueを返します。
   */
  private def checkPart(p: Player): Boolean = {
    val gachaLottery = new GachaLottery
    if (p.getGachaPullNumber % 3000 == 0 && p.getGachaPullNumber != 0) {
      //特等交換券を出す
      p.getWorld.dropItem(p.getLocation(), OriginalItems.tokutoukoukanken)
      p.sendMessage(s"${AQUA}特等交換券が排出されました。")
      return true
    } else if (p.getGachaPullNumber % 1000 == 0 && p.getGachaPullNumber != 0) {
      //必ず何かしらの特等を出す
      p.getWorld.dropItem(p.getLocation(), gachaLottery.itemLottery(Rarity.special))
      return true
    }
    false
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  def onPull(e: PlayerInteractEvent): Unit = {
    val p = e.getPlayer
    val inventory = p.getInventory
    val mainHand = inventory.getItemInMainHand
    val getItemAmount = inventory.getItemInMainHand.getAmount
    val action = e.getAction
    val gachaLottery = new GachaLottery
    if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) &&
      enableGachaTickets.map(_.getItemMeta).contains(mainHand.getItemMeta) && !getCoolDown(p)) {
      val thisGachaPullNum = if (p.isSneaking) getItemAmount else 1
      val rarities: IndexedSeq[Option[Rarity]] = (0 until thisGachaPullNum).map(_ => {
        p.addGachaPullNumber(1)
        if (!checkPart(p)) {
          val rarity = gachaLottery.rarityLottery()
          p.getWorld.dropItem(p.getLocation, gachaLottery.itemLottery(rarity))
          Option(rarity)
        } else {
          None
        }
      })
      if (p.isSneaking) {
        p.sendMessage(s"$AQUA${thisGachaPullNum}回ガチャを引きました！")
        def getRarities(rarity: Rarity): Int = {
          rarities.count(_ == Option(rarity))
        }
        p.sendMessage(s"${AQUA}特等x${getRarities(Rarity.special)},大当たりx${getRarities(Rarity.bigPer)}," +
          s"あたりx${getRarities(Rarity.per)},はずれx${getRarities(Rarity.miss)}")
      } else {
        rarities.head match {
          case Some(rarity) =>
            p.sendMessage(s"$AQUA${rarity.name}！")
          case None =>
        }
      }
      pullCoolDownSet(p, ryoServerAssist)
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      inventory.getItemInMainHand.setAmount(getItemAmount - thisGachaPullNum)
      new GiveTitle().gachaPullNumber(p)
    }
  }

}

package com.ryoserver.Gacha.SubSystems

import com.ryoserver.Gacha.GachaPaperData
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.Item
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player

class GetGachaTickets() {

  def receipt(p: Player): Unit = {
    val gachaTicket = GachaPaperData.normal
    val number = getTickets(p)
    if (number != 0) {
      val oldAmount = p.getInventory.getContents
        .filter(itemStack =>
          (if (itemStack != null) Item.getOneItemStack(itemStack) else null) == Item.getOneItemStack(gachaTicket)).map(_.getAmount).sum
      gachaTicket.setAmount(number)
      p.getInventory.addItem(gachaTicket)
      p.getRyoServerData.removeGachaTicket(p.getInventory.getContents
        .filter(itemStack =>
          (if (itemStack != null) Item.getOneItemStack(itemStack) else null) == Item.getOneItemStack(gachaTicket)).map(_.getAmount).sum - oldAmount).apply(p)
      p.sendMessage(s"${AQUA}ガチャ券を受け取りました。")
      p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    } else {
      p.sendMessage(s"${RED}受け取れるガチャ券がありません！")
    }
  }

  def getTickets(p: Player): Int = {
    val number = p.getRyoServerData.gachaTickets
    if (number >= 576) {
      576
    } else {
      number
    }
  }

}

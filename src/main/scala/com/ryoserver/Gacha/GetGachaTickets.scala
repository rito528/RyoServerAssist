package com.ryoserver.Gacha

import com.ryoserver.Player.{Data, RyoServerPlayer}
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player

class GetGachaTickets() {

  def receipt(p: Player): Unit = {
    val gachaTicket = GachaPaperData.normal
    val number = getTickets(p)
    if (number != 0) {
      gachaTicket.setAmount(number)
      p.getWorld.dropItemNaturally(p.getLocation(), gachaTicket)
      p.sendMessage(s"${AQUA}ガチャ券を受け取りました。")
      p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    } else {
      p.sendMessage(s"${RED}受け取れるガチャ券がありません！")
    }
  }

  def getTickets(p: Player): Int = {
    val number = Data.playerData(p.getUniqueId).gachaTickets
    val rp = new RyoServerPlayer(p)
    if (number >= 576) {
      rp.reduceNormalGachaTicket(576)
      576
    } else {
      rp.reduceNormalGachaTicket(number)
      number
    }
  }

}

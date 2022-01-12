package com.ryoserver.Gacha

import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
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
    val number = p.getGachaTickets
    if (number >= 576) {
      p.reduceNormalGachaTickets(576)
      576
    } else {
      p.reduceNormalGachaTickets(number)
      number
    }
  }

}

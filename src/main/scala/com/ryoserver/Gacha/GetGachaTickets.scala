package com.ryoserver.Gacha

import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player

class GetGachaTickets() {

  def getTickets(p: Player): Int = {
    val rp = new RyoServerPlayer(p)
    val number = Data.playerData(p.getUniqueId).gachaTickets
    if (number >= 576) {
      rp.reduceNormalGachaTicket(576)
      576
    } else {
      rp.reduceNormalGachaTicket(number)
      number
    }
  }

  def receipt(p: Player): Unit = {
    val gachaTicket = GachaPaperData.normal
    val number = getTickets(p)
    if (number != 0) {
      gachaTicket.setAmount(number)
      p.getWorld.dropItemNaturally(p.getLocation(), gachaTicket)
      p.sendMessage(ChatColor.AQUA + "ガチャ券を受け取りました。")
      p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    } else {
      p.sendMessage(ChatColor.RED + "受け取れるガチャ券がありません！")
    }
  }

}

package com.ryoserver.Gacha

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player

class getGachaTickets(ryoServerAssist: RyoServerAssist) {

  def getTickets(p: Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val number = getNumber(p)
    if (number >= (64 * 9)) {
      sql.executeQuery(s"UPDATE Players SET gachaTickets=gachaTickets-${64 * 9} WHERE UUID='${p.getUniqueId.toString}';")
      sql.close()
      64 * 9
    } else {
      sql.executeSQL(s"UPDATE Players SET gachaTickets=0 WHERE UUID='${p.getUniqueId.toString}';")
      sql.close()
      number
    }
  }

  def getNumber(p: Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT gachaTickets FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    var number = 0
    if (rs.next()) number = rs.getInt("gachaTickets")
    sql.close()
    number
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

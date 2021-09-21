package com.ryoserver.Gacha

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class getGachaTickets(ryoServerAssist: RyoServerAssist) {

  def getTickets(p:Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val number = getNumber(p)
    if (number >= (64*9)) {
      sql.executeQuery(s"UPDATE Players SET gachaTickets=gachaTickets-${64*9} WHERE UUID='${p.getUniqueId.toString}';")
      sql.close()
      64*9
    } else {
      sql.executeSQL(s"UPDATE Players SET gachaTickets=0 WHERE UUID='${p.getUniqueId.toString}';")
      sql.close()
      number
    }
  }

  def getNumber(p:Player): Int = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT gachaTickets FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    var number = 0
    if (rs.next()) number = rs.getInt("gachaTickets")
    sql.close()
    number
  }

  def receipt(p:Player): Unit = {
    val gachaTicket = GachaPaperData.normal
    gachaTicket.setAmount(getTickets(p))
    p.getWorld.dropItemNaturally(p.getLocation(),gachaTicket)
    p.sendMessage(ChatColor.AQUA + "ガチャ券を受け取りました。")
  }

}

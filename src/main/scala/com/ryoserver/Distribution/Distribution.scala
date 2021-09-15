package com.ryoserver.Distribution

import com.ryoserver.Gacha.GachaPaperData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, ChatColor}

class Distribution(ryoServerAssist: RyoServerAssist) {

  def createDistributionTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery("SHOW TABLES LIKE 'Distribution';")
    if (!rs.next()) sql.executeSQL("CREATE TABLE Distribution(id INT AUTO_INCREMENT,GachaPaperType TEXT,Count INT, PRIMARY KEY(id));")
    sql.close()
  }

  def addDistribution(gachaPaperType: String, count:Int,sendPlayer:CommandSender): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (gachaPaperType != "normal") {
      sendPlayer.sendMessage(ChatColor.RED + "不明なガチャ券のタイプが指定されました。")
      return
    }
    if (count > 576) {
      sendPlayer.sendMessage(ChatColor.RED + "ガチャ券の配布量は576個を超えないように指定してください！")
      return
    }
    sql.executeSQL(s"INSERT INTO Distribution (GachaPaperType,Count) VALUES ('$gachaPaperType',$count)")
    sql.close()
    sendPlayer.sendMessage(ChatColor.AQUA + "ガチャ券を配布しました！")
    Bukkit.broadcastMessage(ChatColor.YELLOW + s"[お知らせ]${ChatColor.RESET}運営よりガチャ券が配布されました。")
  }

  def receipt(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    var stack = 0
    var id = 0
    var loop = true
    var gachaPaperType = ""
    val rs = sql.executeQuery(s"SELECT * FROM Distribution WHERE (id > (SELECT lastDistributionReceived FROM Players WHERE UUID='${p.getUniqueId.toString}'));")
    while (rs.next() && loop) {
      stack += rs.getInt("Count")
      id = rs.getInt("id")
      if (gachaPaperType == "") gachaPaperType = rs.getString("GachaPaperType")
      if (stack >= 576) loop = false
      else if (gachaPaperType != rs.getString("GachaPaperType")) loop = false
    }
    sql.executeSQL(s"UPDATE Players SET lastDistributionReceived=$id WHERE UUID='${p.getUniqueId.toString}'")
    sql.close()
    var gachaPaper:ItemStack = null
    if (gachaPaperType == "normal") gachaPaper = new ItemStack(GachaPaperData.normal)
    gachaPaper.setAmount(stack)
    p.getWorld.dropItem(p.getLocation(),gachaPaper)
  }

}

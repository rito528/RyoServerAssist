package com.ryoserver.Distribution

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.command.CommandSender
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.entity.Player

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
    sql.executeSQL(s"INSERT INTO Distribution (GachaPaperType,Count) VALUES ('$gachaPaperType',$count)")
    sql.close()
    sendPlayer.sendMessage(ChatColor.AQUA + "ガチャ券を配布しました！")
    Bukkit.broadcastMessage(ChatColor.YELLOW + s"[お知らせ]${ChatColor.RESET}運営よりガチャ券が配布されました。")
  }
}

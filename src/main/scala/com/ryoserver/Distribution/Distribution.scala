package com.ryoserver.Distribution

import com.ryoserver.Gacha.GachaPaperData
import com.ryoserver.Player.{Data, RyoServerPlayer}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.ChatColor._
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, Sound}

class Distribution(ryoServerAssist: RyoServerAssist) {

  def createDistributionTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Distribution(id INT AUTO_INCREMENT,GachaPaperType TEXT,Count INT, PRIMARY KEY(id));")
    sql.close()
  }

  def addDistribution(gachaPaperType: String, count: Int, sendPlayer: CommandSender): Unit = {
    if (gachaPaperType != "normal" && !gachaPaperType.equalsIgnoreCase("fromAdmin")) {
      sendPlayer.sendMessage(s"${RED}不明なガチャ券のタイプが指定されました。")
      return
    }
    if (count > 576) {
      sendPlayer.sendMessage(s"${RED}ガチャ券の配布量は576個を超えないように指定してください！")
      return
    }
    val id = DistributionData.distributionData.last.id + 1
    DistributionData.addedList ::= id
    DistributionData.distributionData ::= DistributionType(id, gachaPaperType, count)
    sendPlayer.sendMessage(s"${AQUA}ガチャ券を配布しました！")
    Bukkit.broadcastMessage(s"$YELLOW[お知らせ]${RESET}運営よりガチャ券が配布されました。")
  }

  def receipt(p: Player): Unit = {
    var stack = 0
    var id = 0
    var loop = true
    var gachaPaperType = ""
    DistributionData.distributionData.foreach(data => {
      val playerData = Data.playerData(p.getUniqueId)
      if (data.id > playerData.lastDistributionReceived) {
        stack += data.amount
        id = data.id
        if (gachaPaperType == "") gachaPaperType = data.TicketType
        if (stack >= 576) loop = false
        else if (gachaPaperType != data.TicketType) loop = false
      }
    })
    if (stack != 0 && id != 0) {
      new RyoServerPlayer(p).setLastDistributionReceived(id)
      var gachaPaper: ItemStack = null
      if (gachaPaperType.equalsIgnoreCase("normal")) gachaPaper = new ItemStack(GachaPaperData.normal)
      if (gachaPaperType.equalsIgnoreCase("fromAdmin")) gachaPaper = new ItemStack(GachaPaperData.fromAdmin)
      gachaPaper.setAmount(stack)
      p.getWorld.dropItem(p.getLocation(), gachaPaper)
      p.sendMessage(s"${AQUA}運営からのガチャ券を受け取りました。")
      p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    }
  }

}

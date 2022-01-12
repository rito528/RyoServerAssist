package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.util.SQL
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Sound}

class CreateData(ryoServerAssist: RyoServerAssist) {

  def createPlayerData(p: Player): Unit = {
    val sql = new SQL()
    if (!sql.connectionTest()) {
      p.sendMessage(s"${RED}プレイヤーデータの作成に失敗しました。")
      sql.close()
      return
    }
    val user_rs = sql.executeQuery(s"SELECT UUID FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    if (!user_rs.next()) {
      sql.executeSQL(s"INSERT INTO Players (UUID,lastLogin,loginDays,consecutiveLoginDays," +
        s"lastDistributionReceived,EXP,Level,questClearTimes,gachaTickets,gachaPullNumber,SkillPoint,SkillOpenPoint,autoStack,VoteNumber) " +
        s"VALUES ('${p.getUniqueId}',NOW(),1,1,(SELECT MAX(id) FROM Distribution),0,0,0,0,0,${new SkillPointCal().getMaxSkillPoint(0)},0,false,0);")
      val inv = p.getInventory
      val items = sql.executeQuery("SELECT ItemStack FROM firstJoinItems;")
      var counter = 0
      if (items.next()) {
        val invData = items.getString("ItemStack").split(";")
        val config = new YamlConfiguration
        invData.foreach(material => {
          config.loadFromString(material)
          inv.setItem(counter, config.getItemStack("i", null))
          counter += 1
        })
      }
      Bukkit.broadcastMessage(s"$AQUA${p.getName}さんが初参加しました！")
      PlayerData.playerData += (p.getUniqueId -> PlayerDataType(0, 0, 0, new SkillPointCal().getMaxSkillPoint(0), 0, 0, 0, 0, 0, 0, None, 0, 0, None, None, None, autoStack = false, None, None, None))
      Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1))
    }
    sql.close()
  }

}

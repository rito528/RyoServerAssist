package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, ChatColor, Sound}

class CreateData(ryoServerAssist: RyoServerAssist) {

  def createPlayerData(p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (!sql.connectionTest()) {
      p.sendMessage(ChatColor.RED + "プレイヤーデータの作成に失敗しました。")
      sql.close()
      return
    }
    //UUID=UUID,lastLogin=最終ログイン,loginDays=ログイン日数,consecutiveLoginDays=連続ログイン日数,lastDistributionReceived=最後に受け取った配布番号)
    sql.executeSQL("CREATE TABLE IF NOT EXISTS Players(UUID Text,lastLogin DATETIME,lastLogout DATETIME,loginDays INT,consecutiveLoginDays INT," +
      "lastDistributionReceived INT,EXP DOUBLE,Level INT,questClearTimes INT,gachaTickets INT,gachaPullNumber INT,SkillPoint INT," +
      "SkillOpenPoint INT,OpenedSkills TEXT,OpenedTitles TEXT,SelectedTitle TEXT,autoStack BOOLEAN,VoteNumber INT);")
    val user_rs = sql.executeQuery(s"SELECT UUID FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    if (!user_rs.next()) {
      sql.executeSQL(s"INSERT INTO Players (UUID,lastLogin,loginDays,consecutiveLoginDays," +
        s"lastDistributionReceived,EXP,Level,questClearTimes,gachaTickets,gachaPullNumber,SkillPoint,SkillOpenPoint,autoStack,VoteNumber) " +
        s"VALUES ('${p.getUniqueId}',NOW(),1,1,(SELECT MAX(id) FROM Distribution),0,0,0,0,0,${new SkillPointCal().getMaxSkillPoint(0)},0,false,0);")
      val inv = p.getInventory
      sql.executeSQL("CREATE TABLE IF NOT EXISTS firstJoinItems(id INT AUTO_INCREMENT,ItemStack TEXT,PRIMARY KEY(`id`));")
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
      Bukkit.broadcastMessage(ChatColor.AQUA + p.getName + "さんが初参加しました！")
      Data.playerData += (p.getUniqueId.toString -> PlayerData(0,0,new SkillPointCal().getMaxSkillPoint(0),Data.playerData.size + 1,0,0,0,0,0,0,None,0,0,None,None,None,autoStack = false,None,None,None))
      Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1))
    }
    sql.close()
  }

}

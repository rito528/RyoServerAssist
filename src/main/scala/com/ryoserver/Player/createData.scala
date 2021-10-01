package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.util.SQL
import org.bukkit.{Bukkit, ChatColor, Sound}
import org.bukkit.entity.Player

class createData(ryoServerAssist: RyoServerAssist) {

  def createPlayerData(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    if (!sql.connectionTest()) {
      p.sendMessage(ChatColor.RED + "プレイヤーデータの作成に失敗しました。")
      sql.close()
      return
    }
    val table_rs = sql.executeQuery("SHOW TABLES LIKE 'Players';")
    //UUID=UUID,lastLogin=最終ログイン,loginDays=ログイン日数,consecutiveLoginDays=連続ログイン日数,lastDistributionReceived=最後に受け取った配布番号)
    if (!table_rs.next()) sql.executeSQL("CREATE TABLE Players(UUID Text,lastLogin DATETIME,loginDays INT,consecutiveLoginDays INT," +
      "lastDistributionReceived INT,EXP INT,Level INT,questClearTimes INT,gachaTickets INT,SkillPoint INT,SkillOpenPoint INT,OpenedSkills TEXT);")
    val user_rs = sql.executeQuery(s"SELECT UUID FROM Players WHERE UUID='${p.getUniqueId.toString}';")
    if (!user_rs.next()) {
      sql.executeSQL(s"INSERT INTO Players (UUID,lastLogin,loginDays,consecutiveLoginDays," +
        s"lastDistributionReceived,EXP,Level,questClearTimes,gachaTickets,SkillPoint,SkillOpenPoint) " +
        s"VALUES ('${p.getUniqueId}',NOW(),1,1,(SELECT MAX(id) FROM Distribution),0,0,0,0,${new SkillPointCal().getMaxSkillPoint(0)},0);")
      Bukkit.broadcastMessage(ChatColor.AQUA + p.getName + "さんが初参加しました！")
      Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1))
    }
    sql.close()
  }

}

package com.ryoserver.Vote

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.setPlayerData
import com.ryoserver.util.{Player, SQL}
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Bukkit, ChatColor, OfflinePlayer, Sound}

class Vote extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    val site = if (e.getVote.getServiceName == "minecraft.jp") "JapanMinecraftServers" else "monocraft"
    val uuid = Player.nameFromUUID(e.getVote.getUsername)
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    })
    val p = Bukkit.getOfflinePlayer(uuid)
    p.giveNormalGachaTickets(16)
    p.addOneVoteNumber()
    updateVoteContinue(p)
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + e.getVote.getUsername + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + getConfig.voteSite.head)
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + getConfig.voteSite(1))
    })
  }

  def updateVoteContinue(p: OfflinePlayer): Unit = {
    val sql = new SQL()
    val query = "UPDATE Players SET ContinueVoteNumber = CASE WHEN DATEDIFF(LastVote, NOW()) <= -1 THEN ContinueVoteNumber + 1 ELSE ContinueVoteNumber " +
      "END," +
      "LastVote = CASE WHEN DATEDIFF(LastVote, NOW()) = -1 THEN ContinueVoteNumber + 1 " +
      "WHEN DATEDIFF(LastVote, NOW()) <> 0 AND DATEDIFF(LastVote, NOW()) <= -1 THEN 0 " +
      "ELSE ContinueVoteNumber " +
      "END," +
      "LastVote = NOW() " +
      s"WHERE UUID='${p.getUniqueId.toString}';"
    sql.executeSQL(query)
    val rs = sql.executeQuery(s"SELECT ContinueVoteNumber FROM Players WHERE UUID =${p.getUniqueId.toString}");
    if (rs.next()) {
      p.setReVoteNumber(rs.getInt("ContinueVoteNumber"))
    }
    sql.close()
  }

}

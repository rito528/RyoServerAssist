package com.ryoserver.Vote

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.util.{Player, SQL}
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Bukkit, ChatColor, OfflinePlayer, Sound}

class Vote extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    vote(e.getVote.getServiceName,e.getVote.getUsername)
  }

  def vote(service: String,name:String): Unit = {
    val site = if (service == "minecraft.jp") "JapanMinecraftServers" else "monocraft"
    val uuid = Player.nameFromUUID(name)
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    })
    val p = Bukkit.getOfflinePlayer(uuid)
    p.addOneVoteNumber()
    updateVoteContinue(p)
    p.giveNormalGachaTickets(16 + p.getReVoteNumber)
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + name + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + getConfig.voteSite.head)
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + getConfig.voteSite(1))
    })
  }

  private def updateVoteContinue(p: OfflinePlayer): Unit = {
    val sql = new SQL()
    val query =
      "UPDATE Players SET ContinueVoteNumber = CASE WHEN DATEDIFF(LastVote, NOW()) <= -1 THEN ContinueVoteNumber + 1 ELSE 1 " +
      "END," +
      "LastVote = NOW() " +
      s"WHERE UUID='${p.getUniqueId.toString}';"
    sql.executeSQL(query)
    val rs = sql.executeQuery(s"SELECT ContinueVoteNumber FROM Players WHERE UUID = '${p.getUniqueId.toString}'");
    if (rs.next()) {
      p.setReVoteNumber(rs.getInt("ContinueVoteNumber"))
    }
    sql.close()
  }

}

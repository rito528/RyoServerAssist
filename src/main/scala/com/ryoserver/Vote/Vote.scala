package com.ryoserver.Vote

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.{Bukkit, Sound}
import org.bukkit.event.{EventHandler, Listener}

class Vote(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    val site = if (e.getVote.getServiceName == "minecraft.jp") "JapanMinecraftServers"
    val uuid = Bukkit.getOfflinePlayer(e.getVote.getUsername).getUniqueId
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation,Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
    })
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + 16,VoteNumber=VoteNumber+1 WHERE UUID='$uuid';")
    sql.close()
    Bukkit.broadcastMessage(site + "で" + e.getVote.getUsername + "さんが投票しました！")
    Bukkit.broadcastMessage("投票はこちら！")
    Bukkit.broadcastMessage(ryoServerAssist.getConfig.getStringList("voteSite").toArray()(0).asInstanceOf[String])
    Bukkit.broadcastMessage(ryoServerAssist.getConfig.getStringList("voteSite").toArray()(1).asInstanceOf[String])
  }

}

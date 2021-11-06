package com.ryoserver.Vote

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.{Bukkit, ChatColor, Sound}
import org.bukkit.event.{EventHandler, Listener}

class Vote(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    val site = if (e.getVote.getServiceName == "minecraft.jp") "JapanMinecraftServers" else "monocraft"
    val uuid = Bukkit.getOfflinePlayer(e.getVote.getUsername).getUniqueId
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    })
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + 16,VoteNumber=VoteNumber+1 WHERE UUID='$uuid';")
    sql.close()
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + e.getVote.getUsername + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(0).asInstanceOf[String])
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(1).asInstanceOf[String])
    })
  }

}

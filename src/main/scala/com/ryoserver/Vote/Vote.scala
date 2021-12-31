package com.ryoserver.Vote

import com.ryoserver.Player.RyoServerPlayer
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Player
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Bukkit, ChatColor, Sound}

class Vote(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    val site = if (e.getVote.getServiceName == "minecraft.jp") "JapanMinecraftServers" else "monocraft"
    val uuid = Player.nameFromUUID(e.getVote.getUsername)
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    })
    val rp = new RyoServerPlayer(Bukkit.getOfflinePlayer(uuid))
    rp.giveNormalGachaTicket(16)
    rp.addOneVoteNumber()
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + e.getVote.getUsername + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(0).asInstanceOf[String])
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(1).asInstanceOf[String])
    })
  }

}

package com.ryoserver.Vote

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.Player
import com.vexsoftware.votifier.model.VotifierEvent
import org.apache.commons.lang.time.DateUtils
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Bukkit, ChatColor, OfflinePlayer, Sound}

import java.util.{Calendar, Date}

class Vote extends Listener {

  @EventHandler
  def onVotifierEvent(e: VotifierEvent): Unit = {
    vote(e.getVote.getServiceName, e.getVote.getUsername)
  }

  def vote(service: String, name: String): Unit = {
    val site = if (service == "minecraft.jp") "JapanMinecraftServers" else "monocraft"
    val uuid = Player.nameFromUUID(name)
    Bukkit.getOnlinePlayers.forEach(onlinePlayer => {
      onlinePlayer.playSound(onlinePlayer.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    })
    implicit val p: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    val ryoServerData = p.getRyoServerData
    ryoServerData.addVoteNumber(1).apply
    updateVoteContinue
    ryoServerData.addGachaTickets(if (ryoServerData.reVoteNumber >= 20) 16 + 20 else 16 + ryoServerData.reVoteNumber)
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + name + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + getConfig.JapanMinecraftServers)
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + getConfig.monocraft)
    })
  }

  private def updateVoteContinue(implicit p: OfflinePlayer): Unit = {
    val lastVoteDay = DateUtils.truncate(p.getRyoServerData.lastVote,Calendar.DAY_OF_MONTH).getTime
    val nowDay = DateUtils.truncate(new Date,Calendar.DAY_OF_MONTH).getTime
    val oneWeekMilliSec = 604800000 //1週間をミリ秒で表す
    val ryoServerData = p.getRyoServerData
    if (nowDay - lastVoteDay <= oneWeekMilliSec) { //1週間以内だったら
      ryoServerData.setReVoteNumber(ryoServerData.reVoteNumber + 1).apply
    } else {
      ryoServerData.setReVoteNumber(0).apply
    }
  }

}

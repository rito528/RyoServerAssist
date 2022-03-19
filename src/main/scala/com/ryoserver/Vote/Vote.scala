package com.ryoserver.Vote

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.util.Player
import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{Bukkit, ChatColor, OfflinePlayer, Sound}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

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
    val p = Bukkit.getOfflinePlayer(uuid)
    p.addOneVoteNumber()
    updateVoteContinue(p)
    p.giveNormalGachaTickets(if (p.getReVoteNumber >= 20) 16 + 20 else 16 + p.getReVoteNumber)
    Bukkit.getOnlinePlayers.forEach(p => {
      p.sendMessage(site + "で" + name + "さんが投票しました！")
      p.sendMessage("投票はこちら！")
      p.sendMessage("JMS: " + ChatColor.UNDERLINE + getConfig.JapanMinecraftServers)
      p.sendMessage("monocraft: " + ChatColor.UNDERLINE + getConfig.monocraft)
    })
  }

  private def updateVoteContinue(p: OfflinePlayer): Unit = {
    implicit val session: AutoSession.type = AutoSession
    sql"""UPDATE Players SET ContinueVoteNumber = CASE WHEN DATEDIFF(LastVote, NOW()) <= -1 THEN ContinueVoteNumber + 1 ELSE ContinueVoteNumber
      END,
      LastVote = NOW()
      WHERE UUID=${p.getUniqueId.toString}""".execute.apply()
    p.getRyoServerData.setReVoteNumber()
    p.setReVoteNumber(sql"SELECT ContinueVoteNumber FROM Players WHERE UUID = ${p.getUniqueId.toString}"
      .map(rs => rs.int("ContinueVoteNumber"))
      .headOption.apply().getOrElse(0))
  }

}

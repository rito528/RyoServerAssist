package com.ryoserver.Player

import com.ryoserver.Player.PlayerData.{PlayerDataRepository, TPlayerDataRepository}
import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, Sound}
import org.bukkit.ChatColor.AQUA
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

class PlayerService {

  private val playerDataRepository: TPlayerDataRepository = new PlayerDataRepository

  def loadPlayerData(uuid: UUID): Unit = {
    playerDataRepository.findBy(uuid) match {
      case None =>
        Bukkit.getOnlinePlayers.forEach(p => {
          p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
        })
        val newPlayer = Bukkit.getPlayer(uuid)
        Bukkit.broadcastMessage(s"$AQUA${newPlayer.getName}さんが初参加しました！")
      case _ =>
    }
    playerDataRepository.restore(uuid)
  }

  def autoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        playerDataRepository.store()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
  }

}

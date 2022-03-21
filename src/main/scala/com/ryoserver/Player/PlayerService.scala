package com.ryoserver.Player

import com.ryoserver.Player.FirstJoin.{FirstJoinGiveItemRepository, TFirstJoinGiveItemRepository}
import com.ryoserver.Player.PlayerData.{PlayerDataRepository, TPlayerDataRepository}
import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, Sound}
import org.bukkit.ChatColor.AQUA
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

class PlayerService {

  private val playerDataRepository: TPlayerDataRepository = new PlayerDataRepository
  private val firstJoinGiveItemRepository: TFirstJoinGiveItemRepository = new FirstJoinGiveItemRepository

  def loadPlayerData(uuid: UUID): Unit = {
    if (!playerDataRepository.restore(uuid)) {
      Bukkit.getOnlinePlayers.forEach(p => {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      })
      val newPlayer = Bukkit.getPlayer(uuid)
      firstJoinGiveItemRepository.get().zipWithIndex.foreach{case (itemStack,index) =>
        newPlayer.getInventory.setItem(index,itemStack)
      }
      Bukkit.broadcastMessage(s"$AQUA${newPlayer.getName}さんが初参加しました！")
    }
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

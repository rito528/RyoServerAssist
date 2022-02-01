package com.ryoserver.Security

import com.ryoserver.Config.ConfigData.getConfig
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

object Operator {

  var once = false

  def checkOp(implicit plugin: Plugin): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        //configで指定されているオペレーター数と、Bukkitで登録されているオペレーター数が一致しなかった場合
        if (getConfig.authority.length != Bukkit.getOperators.size()) {
          if (!once) {
            once = true
            removeOperators(plugin)
            setOperators(plugin)
          }
        }
        //configで指定されているオペレーターと、実際に登録されているプレイヤーが一致しているかどうか
        Bukkit.getOperators.forEach(p => {
          if (!getConfig.authority.mkString("", ", ", "").contains(p.getUniqueId.toString)) {
            if (!once) {
              once = true
              removeOperators(plugin)
              setOperators(plugin)
            }
          }
        })
      }
    }.runTaskTimer(plugin, 0, 5)
  }

  def removeOperators(plugin: Plugin): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        Bukkit.getOperators.forEach(p => {
          p.setOp(false)
        })
      }
    }.runTask(plugin)
  }

  def setOperators(plugin: Plugin): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        getConfig.authority.foreach(uuid => {
          val player = Bukkit.getOfflinePlayer(UUID.fromString(uuid))
          player.setOp(true)
          if (player.isOnline) player.asInstanceOf[Player].sendMessage(s"$YELLOW[OP変更検知] ${AQUA}OPを初期状態に修正しました。")
          once = false
        })
      }
    }.runTask(plugin)
  }

}

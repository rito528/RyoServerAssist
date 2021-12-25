package com.ryoserver.Security

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}

import java.util.UUID

object Operator {

  var once = false

  def checkOp(plugin: Plugin): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        if (Config.getConfigOperators.length != Bukkit.getOperators.size()) {
          if (!once) {
            once = true
            removeOperators(plugin)
            setOperators(plugin)
          }
        }
        Bukkit.getOperators.forEach(p => {
          if (!Config.getConfigOperators.mkString("", ", ", "").contains(p.getUniqueId.toString)) {
            if (!once) {
              once = true
              removeOperators(plugin)
              setOperators(plugin)
            }
          }
        })
      }
    }.runTaskTimer(plugin, 0, 20)
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
        Config.getConfigOperators.foreach(p => {
          val player = Bukkit.getOfflinePlayer(UUID.fromString(p.toString))
          player.setOp(true)
          if (player.isOnline) player.asInstanceOf[Player].sendMessage(ChatColor.YELLOW + "[OP変更検知] " + ChatColor.AQUA + "OPを初期状態に修正しました。")
          once = false
        })
      }
    }.runTask(plugin)
  }

}

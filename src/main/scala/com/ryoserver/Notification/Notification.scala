package com.ryoserver.Notification

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.{EventHandler, Listener}

import java.io.PrintWriter
import java.nio.file.{Files, Paths}

class Notification extends Listener {

  @EventHandler
  def onJoin(e: PlayerJoinEvent): Unit = {
    val notificationConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/Notification.yml").toFile)
    e.getPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', notificationConfig.getStringList("Notification").toArray().mkString("\n")))
  }

}

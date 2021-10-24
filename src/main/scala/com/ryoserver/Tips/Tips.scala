package com.ryoserver.Tips

import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, ChatColor}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitRunnable

import java.nio.file.Paths

class Tips(ryoServerAssist: RyoServerAssist) {

  private var counter = 0

  def sendTips(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        val notificationConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/tips.yml").toFile)
        Bukkit.getOnlinePlayers.forEach(p => p.sendMessage(ChatColor.YELLOW + "[Tips]" + ChatColor.RESET + notificationConfig.getStringList("tipsMsg").get(counter)) )
        counter += 1
        if (notificationConfig.getStringList("tipsMsg").size() <= counter) counter = 0
      }
    }.runTaskTimer(ryoServerAssist,0,ryoServerAssist.getConfig.getInt("tipsTimer") * 20)
  }

}

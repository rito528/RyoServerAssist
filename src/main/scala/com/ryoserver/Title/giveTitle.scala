package com.ryoserver.Title

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.{ChatColor, Sound}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths

class giveTitle(ryoServerAssist: RyoServerAssist) {

  def lv(p:Player): Unit = {
    val level = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
    TitleData.lv.foreach(title => {
      val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
      if (titleConfig.getInt(s"titles.$title.condition") <= level) {
        val data = new PlayerTitleData(ryoServerAssist)
        if (data.openTitle(p.getUniqueId.toString,title)) {
          p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
          p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
        }
      }
    })
  }

}

package com.ryoserver.Title

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.{ChatColor, Sound}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths

class giveTitle(ryoServerAssist: RyoServerAssist) {

  private val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
  private val data = new PlayerTitleData(ryoServerAssist)

  def lv(p:Player): Unit = {
    val level = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
    TitleData.lv.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= level) {
        if (data.openTitle(p.getUniqueId.toString,title)) {
          p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
          p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
        }
      }
    })
  }

  def continuousLogin(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT consecutiveLoginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var continuousLoginDays = 0
    if (rs.next()) continuousLoginDays = rs.getInt("consecutiveLoginDays")
    TitleData.continuousLogin.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.condition") <= continuousLoginDays && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

  def loginDays(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT loginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var LoginDays = 0
    if (rs.next()) LoginDays = rs.getInt("loginDays")
    TitleData.loginDays.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.loginDays") <= LoginDays && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

  def questClearNumber(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT questClearTimes FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var clearTimes = 0
    if (rs.next()) clearTimes = rs.getInt("questClearTimes")
    TitleData.questClearNumber.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.questClearTimes") <= clearTimes && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

  def gachaPullNumber(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT gachaPullNumber FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var pullNumber = 0
    if (rs.next()) pullNumber = rs.getInt("gachaPullNumber")
    TitleData.gachaNumber.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.gachaNumber") <= pullNumber && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

}

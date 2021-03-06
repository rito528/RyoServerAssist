package com.ryoserver.Title

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, TimeZone}
import scala.jdk.CollectionConverters._

class GiveTitle {

  private val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
  private val data = new PlayerTitleData

  def lv(p: Player): Unit = {
    val level = p.getRyoServerData.level
    TitleData.lv.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= level) {
        if (data.openTitle(p, title)) {
          p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
          p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
        }
      }
    })
  }

  def continuousLogin(p: Player): Unit = {
    val continuousLoginDays = p.getRyoServerData.consecutiveLoginDays
    TitleData.continuousLogin.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= continuousLoginDays && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def loginDays(p: Player): Unit = {
    val LoginDays = p.getRyoServerData.loginDays
    TitleData.loginDays.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= LoginDays && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def questClearNumber(p: Player): Unit = {
    val clearTimes = p.getRyoServerData.questClearTimes
    TitleData.questClearNumber.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= clearTimes && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def gachaPullNumber(p: Player): Unit = {
    val pullNumber = p.getRyoServerData.gachaPullNumber
    TitleData.gachaNumber.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= pullNumber && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def skillOpenNumber(p: Player): Unit = {
    TitleData.skillOpen.foreach(title => {
      val diff = titleConfig.getStringList(s"titles.$title.condition").asScala.toSet.diff(p.getRyoServerData.openedSkills.getOrElse(Set.empty).map(_.skillName))
      if (diff.isEmpty && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def loginYear(p: Player): Unit = {
    val year = LocalDateTime.now(ZoneId.of("Asia/Tokyo")).getYear
    TitleData.loginYear.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") == year && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def loginPeriod(p: Player): Unit = {
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    TitleData.loginPeriod.foreach(title => {
      val start = format.parse(s"${titleConfig.getString(s"titles.$title.start")} 00:00")
      val end = format.parse(s"${titleConfig.getString(s"titles.$title.end")} 00:00")
      val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
      if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end) && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def loginDay(p: Player): Unit = {
    val format = new SimpleDateFormat("yyyy/MM/dd")
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).getTime
    TitleData.loginDay.foreach(title => {
      if (titleConfig.getString(s"titles.$title.condition") == format.format(nowCalender) && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def titleGetNumber(p: Player): Unit = {
    TitleData.titleGetNumber.foreach(title => {
      if (data.getHasTitles(p.getUniqueId).toList.length >= titleConfig.getInt(s"titles.$title.condition") && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def continuousLoginAndQuestClearNumber(p: Player): Unit = {
    TitleData.continuousLoginAndQuestClearNumber.foreach(title => {
      val conditionLogin = titleConfig.getString(s"titles.$title.condition").split(",")(0).toInt
      val conditionQuest = titleConfig.getString(s"titles.$title.condition").split(",")(1).toInt
      val login = p.getRyoServerData.consecutiveLoginDays
      val quest = p.getRyoServerData.questClearTimes
      if (conditionLogin <= login && conditionQuest <= quest) {
        if (data.openTitle(p, title)) {
          p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
          p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
        }
      }
    })
  }

}

package com.ryoserver.Title

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, TimeZone}
import scala.collection.mutable
import scala.jdk.CollectionConverters._

class GiveTitle(ryoServerAssist: RyoServerAssist) {

  private val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
  private val data = new PlayerTitleData(ryoServerAssist)

  def lv(p: Player): Unit = {
    val level = p.getQuestLevel
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
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT consecutiveLoginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var continuousLoginDays = 0
    if (rs.next()) continuousLoginDays = rs.getInt("consecutiveLoginDays")
    TitleData.continuousLogin.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= continuousLoginDays && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
    sql.close()
  }

  def loginDays(p: Player): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT loginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var LoginDays = 0
    if (rs.next()) LoginDays = rs.getInt("loginDays")
    TitleData.loginDays.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= LoginDays && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
    sql.close()
  }

  def questClearNumber(p: Player): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT questClearTimes FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var clearTimes = 0
    if (rs.next()) clearTimes = rs.getInt("questClearTimes")
    TitleData.questClearNumber.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= clearTimes && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
    sql.close()
  }

  def gachaPullNumber(p: Player): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT gachaPullNumber FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var pullNumber = 0
    if (rs.next()) pullNumber = rs.getInt("gachaPullNumber")
    TitleData.gachaNumber.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= pullNumber && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
    sql.close()
  }

  def skillOpenNumber(p: Player): Unit = {
    val skillOpenData = p.getOpenedSkills
    skillOpenData match {
      case Some(skills) =>
        TitleData.skillOpen.foreach(title => {
          val conditions: mutable.Map[Integer, Boolean] = mutable.Map.empty[Integer, Boolean]
          titleConfig.getIntegerList(s"titles.$title.condition").asScala.foreach(titleCondition => conditions += (titleCondition -> false))
          skills.split(",").foreach(openedSkill => {
            if (conditions.contains(openedSkill.toInt)) conditions.update(openedSkill.toInt, true)
          })
          var allCheck = true
          conditions.foreach({ case (_, check) =>
            if (!check) allCheck = false
          })
          if (allCheck && data.openTitle(p, title)) {
            p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
          }
        })
      case None =>
    }
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
      if (data.getHasTitles(p.getUniqueId).length >= titleConfig.getInt(s"titles.$title.condition") && data.openTitle(p, title)) {
        p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      }
    })
  }

  def continuousLoginAndQuestClearNumber(p: Player): Unit = {
    TitleData.continuousLoginAndQuestClearNumber.foreach(title => {
      val conditionLogin = titleConfig.getString(s"titles.$title.condition").split(",")(0).toInt
      val conditionQuest = titleConfig.getString(s"titles.$title.condition").split(",")(1).toInt
      val sql = new SQL()
      val loginRs = sql.executeQuery(s"SELECT consecutiveLoginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
      val questRs = sql.executeQuery(s"SELECT questClearTimes FROM Players WHERE UUID='${p.getUniqueId.toString}'")
      var login = 0
      var quest = 0
      if (loginRs.next()) login = loginRs.getInt("consecutiveLoginDays")
      if (questRs.next()) quest = questRs.getInt("questClearTimes")
      if (conditionLogin <= login && conditionQuest <= quest) {
        if (data.openTitle(p, title)) {
          p.sendMessage(s"${AQUA}称号:${title}が開放されました！")
          p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
        }
      }
    })
  }

}

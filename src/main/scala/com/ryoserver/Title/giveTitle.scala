package com.ryoserver.Title

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Sound}

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, TimeZone}
import scala.collection.mutable
import scala.jdk.CollectionConverters._

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
      if (titleConfig.getInt(s"titles.$title.condition") <= continuousLoginDays && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
    sql.close()
  }

  def loginDays(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT loginDays FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var LoginDays = 0
    if (rs.next()) LoginDays = rs.getInt("loginDays")
    TitleData.loginDays.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") <= LoginDays && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
    sql.close()
  }

  def questClearNumber(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT questClearTimes FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var clearTimes = 0
    if (rs.next()) clearTimes = rs.getInt("questClearTimes")
    TitleData.questClearNumber.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.condition") <= clearTimes && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
    sql.close()
  }

  def gachaPullNumber(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT gachaPullNumber FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var pullNumber = 0
    if (rs.next()) pullNumber = rs.getInt("gachaPullNumber")
    TitleData.gachaNumber.foreach(title => {
      if (titleConfig.getInt(s"Titles.$title.condition") <= pullNumber && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
    sql.close()
  }

  def skillOpenNumber(p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery(s"SELECT OpenedSkills FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var skillOpenData = ""
    if (rs.next()) skillOpenData = rs.getString("OpenedSkills")
    TitleData.skillOpen.foreach(title => {
      val conditions:mutable.Map[Integer,Boolean] = mutable.Map.empty[Integer,Boolean]
      titleConfig.getIntegerList(s"titles.$title.condition").asScala.foreach(titleCondition => conditions += (titleCondition -> false))
      skillOpenData.split(",").foreach(openedSkill => {
        if (conditions.contains(openedSkill.toInt)) conditions.update(openedSkill.toInt,true)
      })
      var allCheck = true
      conditions.foreach({case (_,check) =>
        if (!check) allCheck = false
      })
      if (allCheck && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
    sql.close()
  }

  def loginYear(p:Player): Unit = {
    val year = LocalDateTime.now(ZoneId.of("Asia/Tokyo")).getYear
    TitleData.loginYear.foreach(title => {
      if (titleConfig.getInt(s"titles.$title.condition") == year && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

  def loginPeriod(p:Player): Unit = {
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    TitleData.loginPeriod.foreach(title => {
      val start = format.parse(s"${titleConfig.getString(s"titles.$title.start")} 00:00")
      val end = format.parse(s"${titleConfig.getString(s"titles.$title.end")} 00:00")
      val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
      if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end) && data.openTitle(p.getUniqueId.toString,title)) {
          p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
          p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

  def loginDay(p:Player): Unit = {
    val format = new SimpleDateFormat("yyyy/MM/dd")
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).getTime
    TitleData.loginDay.foreach(title => {
      if (titleConfig.getString(s"titles.$title.condition") == format.format(nowCalender) && data.openTitle(p.getUniqueId.toString,title)) {
        p.sendMessage(ChatColor.AQUA + "称号:" + title + "が開放されました！")
        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      }
    })
  }

}

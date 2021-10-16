package com.ryoserver.Level.Player

import com.ryoserver.Level.CalLv
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillOpens.SkillOpenData
import com.ryoserver.SkillSystems.SkillPoint.{SkillPointBer, SkillPointCal, SkillPointData}
import com.ryoserver.Title.giveTitle
import com.ryoserver.util.SQL
import org.apache.commons.lang.time.DateUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player

import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, Date, TimeZone}
import javax.xml.crypto.Data

class updateLevel(ryoServerAssist: RyoServerAssist) {

  def updateExp(exp: Int,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val calLv = new CalLv(ryoServerAssist)
    sql.executeSQL(s"UPDATE Players SET EXP=$exp,Level=${calLv.getLevel(exp)} WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
    BossBar.updateLevelBer(ryoServerAssist,exp, p)
  }

  def addExp(addExp: Double,p:Player): Unit = {
    //levelが上がったときに呼び出されるメソッド
    var exp = addExp
    val now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val start = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 20:00")
    val end = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 21:00")
    val holiday_start = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 14:00")
    val holiday_end = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 15:00")
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end) ||
      ((Calendar.DAY_OF_WEEK == 1 || Calendar.DAY_OF_WEEK == 7) && nowCalender.getTime.after(holiday_start) && nowCalender.getTime.before(holiday_end))) {
      p.sendMessage(ChatColor.AQUA + "ボーナス発生！")
      p.sendMessage(ChatColor.AQUA + "exp量が1.2倍になりました！")
      exp *= 1.2
      p.sendMessage(ChatColor.AQUA + addExp.toString + "->" + String.format("%.2f",exp))
    }
    val sql = new SQL(ryoServerAssist)
    val calLv = new CalLv(ryoServerAssist)
    val data = sql.executeQuery(s"SELECT EXP,Level FROM Players WHERE UUID='${p.getUniqueId.toString}'")
    var old_exp = 0
    var old_level = 0
    if (data.next()) {
      old_exp = data.getInt("EXP")
      old_level = data.getInt("Level")
    }
    val sumExp = exp + old_exp
    val ticketAmountCal = (sumExp / 100) - (old_exp / 100)
    //経験値毎にもらうガチャ券の算出
    if ((ticketAmountCal / 100) <= 1) {
      sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + $ticketAmountCal WHERE UUID='${p.getUniqueId.toString}';")
    }
    //レベル毎にもらうガチャ券の算出
    val nowLevel = calLv.getLevel(sumExp.toInt)
    if (nowLevel > old_level && old_level != 0) {
      for (i <- old_level to nowLevel) {
        if (i % 10 == 0) sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + 32 WHERE UUID='${p.getUniqueId.toString}'")
      }
    }
    sql.executeSQL(s"UPDATE Players SET EXP=EXP + $exp,Level=${calLv.getLevel(sumExp.toInt)} WHERE UUID='${p.getUniqueId.toString}';")
    sql.close()
    BossBar.updateLevelBer(ryoServerAssist,sumExp, p)
    SkillPointBer.update(p,ryoServerAssist)
    new SkillPointData(ryoServerAssist).setSkillPoint(p,new SkillPointCal().getMaxSkillPoint(calLv.getLevel(sumExp.toInt)))
    new SkillOpenData(ryoServerAssist).addSkillOpenPoint(p,nowLevel - old_level)
    if (old_level < nowLevel) {
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "おめでとうございます！レベルが上がりました！")
      p.sendMessage(ChatColor.AQUA + "Lv." + old_level + "→ Lv." + nowLevel)
      new giveTitle(ryoServerAssist).lv(p)
    }
  }
}

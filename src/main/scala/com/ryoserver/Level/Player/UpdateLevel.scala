package com.ryoserver.Level.Player

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Level.CalLv
import com.ryoserver.Player.Name
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.Quest.Event.EventDataProvider
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.{SkillPointBer, SkillPointCal, SkillPointData}
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Sound}

import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, TimeZone}

class UpdateLevel(ryoServerAssist: RyoServerAssist) {


  /*
    Force exp updates
   */
  def updateExp(exp: Int, p: Player): Unit = {
    p.questExpAddNaturally(exp)
    BossBar.updateLevelBer(exp, p)
  }

  /*
    Add exp
   */
  def addExp(addExp: Double, p: Player): Unit = {
    var exp = addExp
    /*
      Check bonus time
     */
    val now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val start = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 20:00")
    val end = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 21:00")
    val holiday_start = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 14:00")
    val holiday_end = format.parse(s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth} 15:00")
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    exp *= EventDataProvider.ratio
    if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end) ||
      ((Calendar.DAY_OF_WEEK == 1 || Calendar.DAY_OF_WEEK == 7) && nowCalender.getTime.after(holiday_start) && nowCalender.getTime.before(holiday_end))) {
      p.sendMessage(s"${AQUA}ボーナス発生！")
      p.sendMessage(s"${AQUA}exp量が1.2倍になりました！")
      exp *= 1.2
      p.sendMessage(s"$AQUA${addExp.toString}->${String.format("%.2f", exp)}")
    }
    /*
      経験値を増やす処理
     */
    val old_exp = p.getQuestExp
    val old_level = p.getQuestLevel
    val calLv = new CalLv

    val sumExp = exp + old_exp
    val ticketAmountCal = (((old_exp % 100) + exp) / 100).toInt
    //経験値毎にもらうガチャ券の算出
    if (ticketAmountCal > 0) p.giveNormalGachaTickets(ticketAmountCal)
    //レベル毎にもらうガチャ券の算出
    val nowLevel = calLv.getLevel(sumExp.toInt)
    if (nowLevel > old_level && old_level != 0) {
      for (i <- old_level + 1 to nowLevel) {
        if (i % 10 == 0) p.giveNormalGachaTickets(32)
      }
    }
    p.questExpAddNaturally(exp)
    BossBar.updateLevelBer(sumExp, p)
    if (old_level < nowLevel) {
      p.addSkillOpenPoint(nowLevel - old_level)
      if (nowLevel > 90) p.addSpecialSkillOpenPoint(nowLevel - old_level)
      //Tab等の表示上の名前を更新
      new Name(ryoServerAssist).updateName(p)
      //スキルポイントを全回復
      new SkillPointData().setSkillPoint(p, new SkillPointCal().getMaxSkillPoint(calLv.getLevel(sumExp.toInt)))
      p.sendMessage(s"${AQUA}おめでとうございます！レベルが上がりました！")
      p.sendMessage(s"${AQUA}Lv." + old_level + " → Lv." + nowLevel)
      val maxLv = getConfig.maxLv
      if (nowLevel == maxLv) {
        Bukkit.broadcastMessage(s"$AQUA${p.getName}さんがLv.${maxLv}に到達しました！")
        Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1))
      }
      new GiveTitle(ryoServerAssist).lv(p)
    }
  }
}

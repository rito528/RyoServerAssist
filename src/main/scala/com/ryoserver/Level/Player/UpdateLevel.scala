package com.ryoserver.Level.Player

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Level.CalLv
import com.ryoserver.Player.Name
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.Quest.Event.EventDataProvider
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, ChatColor, Sound}

import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, TimeZone}

class UpdateLevel(ryoServerAssist: RyoServerAssist) {


  /*
    強制的に経験値を上げる場合に利用する関数
   */
  def updateExp(exp: Int, p: Player): Unit = {
    p.questExpAddNaturally(exp)
    BossBar.updateLevelBer(exp, p)
  }

  /*
    経験値を上げるために利用する関数
   */
  def addExp(addExp: Double, p: Player): Unit = {
    var exp = addExp
    /*
      ボーナスタイムの確認
     */
    val now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
    val nowDate = s"${now.getYear}/${now.getMonthValue}/${now.getDayOfMonth}"
    val start = s"$nowDate 21:00"
    val end = s"$nowDate 22:00"
    val startHoliday = s"$nowDate 14:00"
    val endHoliday = s"$nowDate 15:00"
    if (com.ryoserver.util.Calendar.isBetweenTime(start,end) ||
      ((Calendar.DAY_OF_WEEK == 1 || Calendar.DAY_OF_WEEK == 7) && com.ryoserver.util.Calendar.isBetweenTime(startHoliday,endHoliday))) {
      p.sendMessage(s"${AQUA}ボーナス発生！")
      p.sendMessage(s"${AQUA}exp量が1.2倍になりました！")
      exp *= 1.2
      p.sendMessage(s"$AQUA${String.format("%.2f", addExp)} -> ${String.format("%.2f", exp)}")
    }

    //イベントボーナス分増量
    if (EventDataProvider.ratio != 1.0) {
      p.sendMessage(s"${AQUA}イベントボーナス発生！")
      p.sendMessage(s"${AQUA}経験値が${EventDataProvider.ratio}倍になりました。")
      val oldExp = exp
      exp *= EventDataProvider.ratio
      p.sendMessage(s"$AQUA$oldExp -> ${String.format("%.2f", exp)}")
    }

    //1.0 + (投票日数 * 0.01)だけ経験値を増やすように
    if (p.getReVoteNumber != 0) {
      val doubleExp = (if (p.getReVoteNumber >= 20) 1.0 + (20 * 0.01) else 1.0 + (p.getReVoteNumber * 0.01))
      p.sendMessage(s"${AQUA}連続投票ボーナス発生！")
      p.sendMessage(s"${AQUA}経験値が${doubleExp}倍になりました。")
      val oldExp = exp
      exp *= doubleExp
      p.sendMessage(s"$AQUA$oldExp -> ${String.format("%.2f", exp)}")
    }

    //更新前のレベルと経験値
    val oldPlayerExp = p.getQuestExp
    val oldPlayerLevel = p.getQuestLevel

    //経験値を増やしてレベルバーを更新
    p.questExpAddNaturally(exp)
    BossBar.updateLevelBer(p.getQuestExp, p)

    //経験値毎にもらうガチャ券の枚数を算出して付与
    val ticketAmountCal = (((oldPlayerExp % 100) + exp) / 100).toInt
    if (ticketAmountCal > 0) p.giveNormalGachaTickets(ticketAmountCal)

    //レベルが上がっていた場合の処理
    if (oldPlayerLevel < p.getQuestLevel) {
      for (i <- oldPlayerLevel + 1 to p.getQuestLevel) {
        if (i % 10 == 0) p.giveNormalGachaTickets(32) //Lv10上がるごとにもらえるガチャ券を付与
        if (p.getQuestLevel > 90) p.addSpecialSkillOpenPoint(1) //Lv91からもらえる特殊スキル解放ポイントを付与
        p.addSkillOpenPoint(1) //レベルが上がった分だけスキル解放ポイントを付与
      }

      //Tab等の表示上の名前を更新
      new Name(ryoServerAssist).updateName(p)

      //スキルポイントを全回復
      p.setSkillPoint(new SkillPointCal().getMaxSkillPoint(p.getQuestLevel))

      p.sendMessage(s"${AQUA}おめでとうございます！レベルが上がりました！")
      p.sendMessage(s"${AQUA}Lv." + oldPlayerLevel + " → Lv." + p.getQuestLevel)

      val maxLv = getConfig.maxLv
      //最大レベルに到達した場合のメッセージ
      if (p.getQuestLevel == maxLv) {
        Bukkit.broadcastMessage(s"$AQUA${p.getName}さんがLv.${maxLv}に到達しました！")
        Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1))
      }

      //称号の確認・付与
      new GiveTitle(ryoServerAssist).lv(p)
    }
  }
}

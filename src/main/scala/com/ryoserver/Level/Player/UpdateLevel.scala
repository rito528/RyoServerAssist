package com.ryoserver.Level.Player

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Player.Name
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.Event.EventDataProvider
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Sound}

import java.time.{LocalDateTime, ZoneId}
import java.util.Calendar

class UpdateLevel {


  /*
    強制的に経験値を上げる場合に利用する関数
   */
  def updateExp(exp: Int, p: Player): Unit = {
    p.getRyoServerData.setExp(exp).apply(p)
    BossBar.updateLevelBer(exp, p)
    p.getRyoServerData.setSkillPoint(new SkillPointCal().getMaxSkillPoint(p.getRyoServerData.level)).apply(p)
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
    if (com.ryoserver.util.Calendar.isBetweenTime(start, end) ||
      ((Calendar.DAY_OF_WEEK == 1 || Calendar.DAY_OF_WEEK == 7) && com.ryoserver.util.Calendar.isBetweenTime(startHoliday, endHoliday))) {
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
      p.sendMessage(s"$AQUA${String.format("%.2f", oldExp)} -> ${String.format("%.2f", exp)}")
    }

    //1.0 + (投票日数 * 0.01)だけ経験値を増やすように
    if (p.getRyoServerData.reVoteNumber != 0) {
      val doubleExp = (if (p.getRyoServerData.reVoteNumber >= 20) 1.0 + (20 * 0.01) else 1.0 + (p.getRyoServerData.reVoteNumber * 0.01))
      p.sendMessage(s"${AQUA}連続投票ボーナス発生！")
      p.sendMessage(s"${AQUA}経験値が${String.format("%.2f", doubleExp)}倍になりました。")
      val oldExp = exp
      exp *= doubleExp
      p.sendMessage(s"$AQUA${String.format("%.2f", oldExp)} -> ${String.format("%.2f", exp)}")
    }

    //更新前のレベルと経験値
    val oldPlayerExp = p.getRyoServerData.exp
    val oldPlayerLevel = p.getRyoServerData.level

    //経験値を増やしてレベルバーを更新
    p.getRyoServerData.addExp(exp).apply(p)
    BossBar.updateLevelBer(p.getRyoServerData.exp, p)

    //経験値毎にもらうガチャ券の枚数を算出して付与
    val ticketAmountCal = (((oldPlayerExp % 100) + exp) / 100).toInt
    if (ticketAmountCal > 0) p.getRyoServerData.addGachaTickets(ticketAmountCal).apply(p)

    //レベルが上がっていた場合の処理
    if (oldPlayerLevel < p.getRyoServerData.level) {
      for (i <- oldPlayerLevel + 1 to p.getRyoServerData.level) {
        if (i % 10 == 0) p.getRyoServerData.addGachaTickets(32).apply(p) //Lv10上がるごとにもらえるガチャ券を付与
        if (p.getRyoServerData.level > 90) p.getRyoServerData.addSpecialSkillOpenPoint(1).apply(p) //Lv91からもらえる特殊スキル解放ポイントを付与
        p.getRyoServerData.addSkillOpenPoint(1) //レベルが上がった分だけスキル解放ポイントを付与
      }

      //Tab等の表示上の名前を更新
      new Name().updateName(p)

      //スキルポイントを全回復
      p.getRyoServerData.setSkillPoint(new SkillPointCal().getMaxSkillPoint(p.getRyoServerData.level)).apply(p)

      p.sendMessage(s"${AQUA}おめでとうございます！レベルが上がりました！")
      p.sendMessage(s"${AQUA}Lv." + oldPlayerLevel + " → Lv." + p.getRyoServerData.level)

      val maxLv = getConfig.maxLv
      //最大レベルに到達した場合のメッセージ
      if (p.getRyoServerData.level == maxLv) {
        Bukkit.broadcastMessage(s"$AQUA${p.getName}さんがLv.${maxLv}に到達しました！")
        Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1))
      }

      //称号の確認・付与
      new GiveTitle().lv(p)
    }
  }
}

package com.ryoserver.Quest.Event

import com.ryoserver.Player.PlayerData
import com.ryoserver.Player.PlayerManager.setPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Logger.getLogger
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone, UUID}

class EventGateway(implicit ryoServerAssist: RyoServerAssist) {

  def loadEventData(): Unit = {
    if (holdingEvent() != null) {
      getLogger.info("イベント情報を読み込み中...")
      val info = eventInfo(holdingEvent())
      if (info.eventType != "bonus") {
        val sql = new SQL()
        val rs = sql.executeQuery(s"SELECT * FROM Events WHERE EventName='${holdingEvent()}';")
        if (rs.next()) EventDataProvider.eventCounter = rs.getInt("counter")
        sql.close()
      } else {
        EventDataProvider.ratio = info.exp
      }
      getLogger.info("イベント情報の読み込みが完了しました。")
    }
  }

  /*
    イベントの細かい情報を返す
   */
  def eventInfo(eventName: String): EventType = {
    val data = EventDataProvider.eventData.filter(_.name == eventName)
    if (data.length == 0) {
      null
    } else {
      data.head
    }
  }

  def loadEventRanking(): Unit = {
    if (holdingEvent() != null) {
      getLogger.info("イベントランキングを読み込み中...")
      val sql = new SQL()
      val rs = sql.executeQuery(s"SELECT * FROM EventRankings WHERE EventName='${holdingEvent()}';")
      while (rs.next()) EventDataProvider.eventRanking += (rs.getString("UUID") -> rs.getInt("counter"))
      sql.close()
      getLogger.info("イベントランキングの読み込みが完了しました。")
    }
  }

  /*
    イベントが開催されていればイベント名、されていなければnullを返す
   */
  def holdingEvent(): String = {
    EventDataProvider.eventData.foreach(event => {
      val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
      val start = format.parse(s"${event.start} 15:00")
      val end = format.parse(s"${event.end} 20:59")
      val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
      if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end)) return event.name
    })
    null
  }

  /*
    終わったイベントかつボーナスイベントではないもののデータを取得する
   */
  def loadBeforeEvents(): Unit = {
    val sql = new SQL()
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    EventDataProvider.eventData.foreach { eventData =>
      val end = format.parse(s"${eventData.end} 20:59")
      if (nowCalender.getTime.after(end) && eventData.eventType != "bonus") {
        val rs = sql.executeQuery(s"SELECT * FROM EventRankings WHERE EventName='${eventData.name}';")
        EventDataProvider.oldEventData += (eventData.name -> Iterator.from(0).takeWhile(_ => rs.next())
          .map(_ => UUID.fromString(rs.getString("UUID")) -> rs.getInt("counter")).toMap)
      }
    }
    sql.close()
  }

  /*
    イベントの開催を確認する
   */
  def autoCheckEvent(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        if (holdingEvent() != null && eventInfo(holdingEvent()).eventType == "bonus") {
          EventDataProvider.ratio = eventInfo(holdingEvent()).exp
        } else {
          EventDataProvider.ratio = 1.0
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 20, 20)
  }

  def autoSaveEvent(): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        saveEvent()
        saveRanking()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, oneMinute, oneMinute)
  }

  def saveEvent(): Unit = {
    if (holdingEvent() != null && eventInfo(holdingEvent()).eventType != "bonus") {
      EventDataProvider.nowEventName = holdingEvent()
      val sql = new SQL()
      val rs = sql.executeQuery(s"SELECT * FROM Events WHERE EventName='${holdingEvent()}'")
      var oldExp = 0
      if (rs.next()) oldExp = rs.getInt("counter")
      val reward = EventDataProvider.eventData.filter(_.name == holdingEvent()).head.reward
      val givenGachaTicketData = sql.executeQuery(s"SELECT GivenGachaTickets FROM Events WHERE EventName='${holdingEvent()}'")
      givenGachaTicketData.next()
      val gacha = ((EventDataProvider.eventCounter / reward) * EventDataProvider.eventData.filter(_.name == holdingEvent()).head.distribution) - givenGachaTicketData.getInt("GivenGachaTickets")
      sql.executeSQL(s"INSERT INTO Events(EventName,counter,GivenGachaTickets) VALUES ('${holdingEvent()}',${EventDataProvider.eventCounter},${gacha}) " +
        s"ON DUPLICATE KEY UPDATE counter=${EventDataProvider.eventCounter},GivenGachaTickets=GivenGachaTickets+${gacha}")
      PlayerData.playerData.foreach { case (uuid, _) =>
        val offlinePlayer = Bukkit.getOfflinePlayer(uuid)
        offlinePlayer.giveNormalGachaTickets(gacha)
      }
      sql.close()
    }
  }

  def saveRanking(): Unit = {
    if ((holdingEvent() != null && eventInfo(holdingEvent()).eventType != "bonus") || isEventEnded) {
      val sql = new SQL()
      sql.executeSQL(s"DELETE FROM EventRankings WHERE EventName='${holdingEvent()}'")
      EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.zipWithIndex.foreach { case ((uuid, counter), index) =>
        if (isEventEnded) {
          index match {
            case 0 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.YELLOW}${ChatColor.BOLD}1位${ChatColor.RESET}")
            case 1 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.AQUA}${ChatColor.BOLD}2位${ChatColor.RESET}")
            case 2 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.GREEN}${ChatColor.BOLD}3位${ChatColor.RESET}")
            case _ => addEventRankingTitle(uuid, EventDataProvider.nowEventName)
          }
          sql.executeSQL(s"INSERT INTO EventRankings (UUID,EventName,counter) VALUES ('$uuid','${EventDataProvider.nowEventName}',$counter + 1);")
        } else {
          sql.executeSQL(s"INSERT INTO EventRankings (UUID,EventName,counter) VALUES ('$uuid','${holdingEvent()}',$counter + 1);")
        }
      }
      if (isEventEnded) EventDataProvider.nowEventName = ""
      sql.close()
    }
  }

  def isEventEnded: Boolean = {
    if (EventDataProvider.nowEventName != "" && holdingEvent() == null) {
      return true
    } else if (holdingEvent() != null) {
      EventDataProvider.nowEventName = holdingEvent()
      return false
    }
    false
  }

  def addEventRankingTitle(uuid: String, titleName: String): Unit = {
    val alreadyTitles = getEventRankingTitles(uuid)
    var titles = if (alreadyTitles != null && alreadyTitles.length == 1) alreadyTitles.head + ";" else if (alreadyTitles != null) alreadyTitles.mkString(";") + ";" else ""
    titles += titleName + ";"
    val sql = new SQL()
    sql.executeSQL(s"UPDATE Players SET EventTitles='$titles' WHERE UUID='$uuid';")
    sql.close()
  }

  def getEventRankingTitles(uuid: String): List[String] = {
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT EventTitles FROM Players WHERE UUID='$uuid'")
    if (!rs.next()) return null
    if (rs.getString("EventTitles") != null) {
      val titles = rs.getString("EventTitles").split(";").toList
      sql.close()
      titles
    } else {
      sql.close()
      null
    }
  }

}

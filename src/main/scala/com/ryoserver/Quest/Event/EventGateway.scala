package com.ryoserver.Quest.Event

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

class EventGateway(ryoServerAssist: RyoServerAssist) {

  /*
    イベントが開催されていればイベント名、されていなければnullを返す
   */
  def holdingEvent(): String = {
    EventDataProvider.eventData.foreach(event => {
      val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
      val start = format.parse(s"${event.start} 15:00")
      val end = format.parse(s"${event.end} 21:00")
      val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
      if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end)) return event.name
    })
    null
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

  private def createEventTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"CREATE TABLE IF NOT EXISTS Events(EventName TEXT NOT NULL,counter INT, PRIMARY KEY(EventName(64)));")
    sql.close()
  }

  private def createEventRankingTable(): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"CREATE TABLE IF NOT EXISTS EventRankings(UUID TEXT, EventName TEXT, counter INT)")
    sql.close()
  }

  def loadEventData(): Unit = {
    if (holdingEvent() != null) {
      ryoServerAssist.getLogger.info("イベント情報を読み込み中...")
      val sql = new SQL(ryoServerAssist)
      val rs = sql.executeQuery(s"SELECT * FROM Events WHERE EventName='${holdingEvent()}';")
      if (rs.next()) EventDataProvider.eventCounter = rs.getInt("counter")
      ryoServerAssist.getLogger.info("イベント情報の読み込みが完了しました。")
    }
  }

  def loadEventRanking(): Unit = {
    if (holdingEvent() != null) {
      ryoServerAssist.getLogger.info("イベントランキングを読み込み中...")
      createEventRankingTable()
      val sql = new SQL(ryoServerAssist)
      val rs = sql.executeQuery(s"SELECT * FROM EventRankings WHERE EventName='${holdingEvent()}';")
      while (rs.next()) EventDataProvider.eventRanking += (rs.getString("UUID") -> rs.getInt("counter"))
      sql.close()
      ryoServerAssist.getLogger.info("イベントランキングの読み込みが完了しました。")
    }
  }

  def autoSaveEvent(): Unit = {
    val oneMinute = 1200
    new BukkitRunnable {
      override def run(): Unit = {
        saveEvent()
        saveRanking()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,oneMinute,oneMinute)
  }

  def saveEvent(): Unit = {
    if (holdingEvent() != null) {
      createEventTable()
      val sql = new SQL(ryoServerAssist)
      val rs = sql.executeQuery(s"SELECT * FROM Events WHERE EventName='${holdingEvent()}'")
      var oldExp = 0
      if (rs.next()) oldExp = rs.getInt("counter")
      val reward = EventDataProvider.eventData.filter(_.name == holdingEvent()).head.reward
      val gacha = (EventDataProvider.eventCounter / reward) - (oldExp / reward)
      sql.executeSQL(s"INSERT INTO Events(EventName,counter) VALUES ('${holdingEvent()}',${EventDataProvider.eventCounter}) " +
        s"ON DUPLICATE KEY UPDATE counter=${EventDataProvider.eventCounter}")
      sql.executeSQL(s"UPDATE Players SET gachaTickets=gachaTickets + $gacha")
      sql.close()
    }
  }

  def saveRanking(): Unit = {
    if (holdingEvent() != null) {
      createEventRankingTable()
      val sql = new SQL(ryoServerAssist)
      sql.executeSQL(s"DELETE FROM EventRankings WHERE EventName='${holdingEvent()}'")
      var sqlText = ""
      EventDataProvider.eventRanking.foreach{case (uuid,counter) =>
        sqlText += s"INSERT INTO EventRankings (UUID,EventName,counter) VALUES ('$uuid','${holdingEvent()}',$counter);"
      }
      sql.executeSQL(sqlText)
      sql.close()
    }
  }

}

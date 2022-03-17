package com.ryoserver.Quest.Event

import com.ryoserver.Player.PlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}
import scalikejdbc.{AutoSession, DB, scalikejdbcSQLInterpolationImplicitDef}

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone, UUID}

class EventGateway(implicit ryoServerAssist: RyoServerAssist) {

  def loadEventData(): Unit = {
    if (holdingEvent() != null) {
      ryoServerAssist.getLogger.info("イベント情報を読み込み中...")
      val info = eventInfo(holdingEvent())
      if (info.eventType != "bonus") {
        implicit val session: AutoSession.type = AutoSession
        val eventsTable = sql"SELECT * FROM Events WHERE EventName=${holdingEvent()}"
        if (eventsTable.getHeadData.nonEmpty) {
          eventsTable.foreach(rs => {
            EventDataProvider.eventCounter = rs.int("counter")
          })
        }
      } else {
        EventDataProvider.ratio = info.exp
      }
      ryoServerAssist.getLogger.info("イベント情報の読み込みが完了しました。")
    }
  }

  def loadEventRanking(): Unit = {
    if (holdingEvent() != null) {
      ryoServerAssist.getLogger.info("イベントランキングを読み込み中...")
      implicit val session: AutoSession.type = AutoSession
      val eventRankingsTable = sql"SELECT * FROM EventRankings WHERE EventName=${holdingEvent()}"
      eventRankingsTable.foreach(rs => {
        EventDataProvider.eventRanking += (rs.string("UUID") -> rs.int("counter"))
      })
      ryoServerAssist.getLogger.info("イベントランキングの読み込みが完了しました。")
    }
  }

  /*
    終わったイベントかつボーナスイベントではないもののデータを取得する
   */
  def loadBeforeEvents(): Unit = {
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    EventDataProvider.eventData.foreach { eventData =>
      val end = format.parse(s"${eventData.end} 20:59")
      if (nowCalender.getTime.after(end) && eventData.eventType != "bonus") {
        implicit val session: AutoSession.type = AutoSession
        EventDataProvider.oldEventData += (eventData.name -> sql"SELECT * FROM EventRankings WHERE EventName=${eventData.name}"
          .map(rs => UUID.fromString(rs.string("UUID")) -> rs.int("counter")).toList.apply().toMap)
      }
    }
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
      implicit val session: AutoSession.type = AutoSession
      var oldExp = 0
      sql"SELECT * FROM Events WHERE EventName=${holdingEvent()}".foreach(rs => {
        oldExp = rs.int("counter")
      })
      val reward = EventDataProvider.eventData.filter(_.name == holdingEvent()).head.reward
      sql"SELECT GivenGachaTickets FROM Events WHERE EventName=${holdingEvent()}".foreach(rs => {
        val gacha = ((EventDataProvider.eventCounter / reward) * EventDataProvider.eventData.filter(_.name == holdingEvent()).head.distribution) - rs.int("GivenGachaTickets")
        sql"""INSERT INTO Events(EventName,counter,GivenGachaTickets) VALUES (${holdingEvent()},${EventDataProvider.eventCounter},$gacha)
          ON DUPLICATE KEY UPDATE counter=${EventDataProvider.eventCounter},GivenGachaTickets=GivenGachaTickets+$gacha"""
          .execute.apply()
//        PlayerData.playerData.foreach { case (uuid, _) =>
//          val offlinePlayer = Bukkit.getOfflinePlayer(uuid)
//          offlinePlayer.giveNormalGachaTickets(gacha)
//        }
      })
    }
  }

  def saveRanking(): Unit = {
    if ((holdingEvent() != null && eventInfo(holdingEvent()).eventType != "bonus") || isEventEnded) {
      DB.localTx(implicit session => {
        sql"DELETE FROM EventRankings WHERE EventName=${holdingEvent()}".execute.apply()
        EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.zipWithIndex.foreach { case ((uuid, counter), index) =>
          if (isEventEnded) {
            index match {
              case 0 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.YELLOW}${ChatColor.BOLD}1位${ChatColor.RESET}")
              case 1 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.AQUA}${ChatColor.BOLD}2位${ChatColor.RESET}")
              case 2 => addEventRankingTitle(uuid, EventDataProvider.nowEventName + s" - ${ChatColor.GREEN}${ChatColor.BOLD}3位${ChatColor.RESET}")
              case _ => addEventRankingTitle(uuid, EventDataProvider.nowEventName)
            }
            sql"INSERT INTO EventRankings (UUID,EventName,counter) VALUES ($uuid,${EventDataProvider.nowEventName},$counter + 1);".execute.apply()
          } else {
            sql"INSERT INTO EventRankings (UUID,EventName,counter) VALUES ($uuid,${holdingEvent()},$counter + 1);".execute.apply()
          }
        }
        if (isEventEnded) EventDataProvider.nowEventName = ""
      })
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

  def isEventEnded: Boolean = {
    if (EventDataProvider.nowEventName != "" && holdingEvent() == null) {
      return true
    } else if (holdingEvent() != null) {
      EventDataProvider.nowEventName = holdingEvent()
      return false
    }
    false
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

  def addEventRankingTitle(uuid: String, titleName: String): Unit = {
    val alreadyTitles = getEventRankingTitles(uuid)
    var titles = if (alreadyTitles != null && alreadyTitles.length == 1) alreadyTitles.head + ";" else if (alreadyTitles != null) alreadyTitles.mkString(";") + ";" else ""
    titles += titleName + ";"
    implicit val session: AutoSession.type = AutoSession
    sql"UPDATE Players SET EventTitles=$titles WHERE UUID=$uuid;".execute.apply()
  }

  def getEventRankingTitles(uuid: String): List[String] = {
    implicit val session: AutoSession.type = AutoSession
    val eventTitlesTable = sql"SELECT EventTitles FROM Players WHERE UUID=$uuid"
    if (eventTitlesTable.getHeadData.isEmpty) return null
    eventTitlesTable.foreach(rs => {
      if (rs.string("EventTitles") != null) {
        val titles = rs.string("EventTitles").split(";").toList
        return titles
      } else {
        return null
      }
    })
    null
  }

}

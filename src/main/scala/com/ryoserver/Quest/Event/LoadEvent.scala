package com.ryoserver.Quest.Event

import com.ryoserver.Quest.Event.EventData.startingEvent
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitRunnable

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

class LoadEvent(ryoServerAssist: RyoServerAssist) {

  private val EVENT_SETTING_FILE = "plugins/RyoServerAssist/event.yml"

  def load(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        val eventConfig = YamlConfiguration.loadConfiguration(Paths.get(EVENT_SETTING_FILE).toFile)
        eventConfig.getConfigurationSection("").getKeys(false).forEach(eventName => {
          val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
          val start = format.parse(s"${eventConfig.getString(eventName + ".start")} 00:00")
          val end = format.parse(s"${eventConfig.getString(eventName + ".end")} 17:52")
          val calendar = Calendar.getInstance()
          calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
          val nowCalender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
          println(start)
          println(end)
          println(nowCalender)
          println(nowCalender.getTime.after(start))
          println(nowCalender.getTime.before(end))
          if (nowCalender.getTime.after(start) && nowCalender.getTime.before(end)) {
            //開催中
            if (startingEvent != eventName) {
              startingEvent = eventName
              Bukkit.broadcastMessage("イベント:" + eventName + "が開催中です！")
            }
          } else if (startingEvent != "" && !nowCalender.before(format.parse(s"${eventConfig.getString(startingEvent + ".end")} 17:52"))) {
            //別のイベントが開催中で、そのイベントが終了した
            Bukkit.broadcastMessage("イベント:" + startingEvent + "が終了しました！")
            startingEvent = ""
          }
        })
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,0,20*60)
  }

}

package com.ryoserver.util

import java.text.SimpleDateFormat
import java.util.TimeZone

object Calendar {

  def isBetweenTime(start: String,end: String): Boolean = {
    val regex = "[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}".r
    require(!regex.matches(start) || !regex.matches(end),"時間の指定フォーマットが違います。yyyy/MM/dd HH:mm")
    val nowCalender = java.util.Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    nowCalender.getTime.after(format.parse(start)) && nowCalender.getTime.before(format.parse(end))
  }

}

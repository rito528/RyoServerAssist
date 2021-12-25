package com.ryoserver.Distribution

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL

class LoadDistribution(ryoServerAssist: RyoServerAssist) {

  def load(): Unit = {
    val sql = new SQL(ryoServerAssist)
    val rs = sql.executeQuery("SELECT * FROM Distribution")
    DistributionData.distributionData = Iterator.from(0).takeWhile(_ => rs.next())
      .map(_ => DistributionType(rs.getInt("id"),
        rs.getString("GachaPaperType"),
        rs.getInt("Count"))).toList
    sql.close()
  }

}

package com.ryoserver.Distribution

import com.ryoserver.util.SQL

class LoadDistribution {

  def load(): Unit = {
    val sql = new SQL()
    val rs = sql.executeQuery("SELECT * FROM Distribution")
    DistributionData.distributionData = Iterator.from(0).takeWhile(_ => rs.next())
      .map(_ => DistributionType(rs.getInt("id"),
        rs.getString("GachaPaperType"),
        rs.getInt("Count"))).toList
    if (DistributionData.distributionData.isEmpty) DistributionData.distributionData = List(DistributionType(0, "normal", 0))
    sql.close()
  }

}

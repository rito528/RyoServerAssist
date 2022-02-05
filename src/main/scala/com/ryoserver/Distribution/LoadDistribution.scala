package com.ryoserver.Distribution

import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class LoadDistribution {

  def load(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    DistributionData.distributionData = sql"SELECT * FROM Distribution"
      .map(rs => DistributionType(rs.int("id"),
        rs.string("GachaPaperType"),
        rs.int("Count"))).toList.apply()
    if (DistributionData.distributionData.isEmpty) DistributionData.distributionData = List(DistributionType(0, "normal", 0))
  }

}

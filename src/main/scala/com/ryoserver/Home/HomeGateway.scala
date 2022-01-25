package com.ryoserver.Home

import org.bukkit.Location

import java.util.UUID

class HomeGateway(uuid: UUID) {

  def setHomePoint(point: Int,location: Location): Unit = {
    HomeData.removeHomeData(uuid,point)
    HomeData.addHomeData(HomeDataType(
      UUID = uuid,
      point = point,
      location = location,
      isLocked = false
    ))
  }

}

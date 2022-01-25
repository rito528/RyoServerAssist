package com.ryoserver.Home

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class HomeGateway(p: Player) {

  private val uuid = p.getUniqueId

  def setHomePoint(point: Int,location: Location): Unit = {
    HomeData.swapHomeData(uuid,point,HomeDataType(
      UUID = uuid,
      point = point,
      location = location,
      isLocked = false
    ))
    p.sendMessage(s"${AQUA}ホーム${point}を設定しました。")
  }

  def toggleLock(point: Int): Unit = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        HomeData.swapHomeData(uuid, point, data.copy(isLocked = !data.isLocked))
        p.sendMessage(s"${AQUA}ホーム$point${if (isHomeLocked(point)) "のロックを解除しました。" else "をロックしました。"}")
      case None =>
        p.sendMessage(s"${RED}ホームが設定されていないためロックできませんでした。")
    }
  }

  def teleportHome(point: Int): Unit = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        p.teleport(data.location)
        p.sendMessage(s"${AQUA}ホーム${point}にテレポートしました。")
      case None =>
        p.sendMessage(s"${RED}ホームが設定されていないためテレポートできませんでした。")
    }
  }

  def isHomeLocked(point: Int): Boolean = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        data.isLocked
      case None =>
        false
    }
  }

  def getLocationString(point: Int): Option[String] = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        val location = data.location
        Option(s"${location.getWorld.getName},${location.getX},${location.getY},${location.getZ}")
      case None =>
        None
    }
  }

}

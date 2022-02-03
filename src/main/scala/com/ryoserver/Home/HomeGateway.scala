package com.ryoserver.Home

import org.bukkit.ChatColor._
import org.bukkit.Location
import org.bukkit.entity.Player

class HomeGateway(p: Player) {

  private val uuid = p.getUniqueId

  def setHomePoint(point: Int, location: Location): Unit = {
    if (isHomeLocked(point)) {
      p.sendMessage(s"${RED}ホーム${point}がロックされています。")
      return
    }
    HomeData.swapHomeData(uuid, point, HomeDataType(
      UUID = uuid,
      point = point,
      location = location,
      isLocked = false
    ))
    p.sendMessage(s"${AQUA}ホーム${point}を設定しました。")
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

  def toggleLock(point: Int): Unit = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        HomeData.swapHomeData(uuid, point, data.copy(isLocked = !data.isLocked))
        p.sendMessage(s"${AQUA}ホーム$point${if (!isHomeLocked(point)) "のロックを解除しました。" else "をロックしました。"}")
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

  def getLocationString(point: Int): Option[String] = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData match {
      case Some(data) =>
        val location = data.location
        Option(s"${location.getWorld.getName},${location.getX.toInt},${location.getY.toInt},${location.getZ.toInt}")
      case None =>
        None
    }
  }

}

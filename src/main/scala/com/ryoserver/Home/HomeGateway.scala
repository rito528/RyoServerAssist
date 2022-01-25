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
    HomeData.swapHomeData(uuid, point, targetData.copy(isLocked = !targetData.isLocked))
    p.sendMessage(s"${AQUA}ホーム$point${if (isHomeLocked(point)) "のロックを解除しました。" else "をロックしました。"}")
  }

  def teleportHome(point: Int): Unit = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    p.teleport(targetData.location)
    p.sendMessage(s"${AQUA}ホーム${point}にテレポートしました。")
  }

  def isHomeLocked(point: Int): Boolean = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    targetData.isLocked
  }

  def getLocationString(point: Int): String = {
    val targetData = HomeData.getTargetHomeData(uuid, point)
    val location = targetData.location
    s"${location.getWorld.getName},${location.getX},${location.getY},${location.getZ}"
  }

}

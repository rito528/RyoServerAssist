package com.ryoserver.Quest.Event

import com.ryoserver.Menu.{Menu, CreateMenu}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, Material}
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

import java.util.UUID

class EventRankingMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override var name: String = "イベントランキング"
  override val slot: Int = 6
  override var p: Player = _

  def openRankingMenu(player: Player): Unit = {
    p = player
    val gateway = new EventGateway(ryoServerAssist)
    EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.zipWithIndex.foreach{case ((uuid,counter),index) =>
      if (index / 9 + 1 >= 6) return
      val OfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid))
      setSkullItem(index % 9 + 1, (index / 9) + 1,OfflinePlayer.getPlayer,s"${AQUA}[${index + 1}位] " + OfflinePlayer.getName,List(
        if (gateway.eventInfo(gateway.holdingEvent()).eventType == "delivery") s"${WHITE}納品量: " + counter + "個"
        else if (gateway.eventInfo(gateway.holdingEvent()).eventType == "suppression") s"${WHITE}討伐数: " + counter + "体"
        else null
      ).filterNot(_ == null))
    }
    setItem(1,6,Material.MAGENTA_GLAZED_TERRACOTTA,effect = false,s"${YELLOW}イベントメニューに戻ります。",List(s"${GRAY}クリックで戻ります。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    if (getLayOut(1,6) == index) {
      new EventMenu(ryoServerAssist).openEventMenu(p)
    }
  }


}

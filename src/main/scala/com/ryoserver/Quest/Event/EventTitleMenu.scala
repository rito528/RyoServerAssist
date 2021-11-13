package com.ryoserver.Quest.Event

import com.ryoserver.Menu.{CreateMenu, Menu}
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.PlayerTitleData
import org.bukkit.{ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class EventTitleMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override var name: String = "イベント称号"
  override val slot: Int = 6
  override var p: Player = _

  def openEventTitleMenu(player:Player): Unit = {
    p = player
    setItem(1,6,Material.MAGENTA_GLAZED_TERRACOTTA,effect = false,s"${GREEN}イベントメニューに戻ります。",List(s"${GRAY}クリックで戻ります。"))
    val eventGateway = new EventGateway(ryoServerAssist)
    if (eventGateway.getEventRankingTitles(player.getUniqueId.toString) != null) {
      eventGateway.getEventRankingTitles(player.getUniqueId.toString).zipWithIndex.foreach { case (title, index) =>
        setItem(getX(index), getY(index), Material.NAME_TAG, effect = false, RESET + title, List(s"${GRAY}クリックで設定します。"))
      }
    }
    setItem(5,6,Material.PAPER,effect = false,s"${GREEN}称号の設定をリセットします。",List(s"${GRAY}クリックでリセットします。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    if (index <= 43 && p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val titleName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName + ChatColor.RESET
      new PlayerTitleData(ryoServerAssist).setSelectTitle(p.getUniqueId.toString, titleName)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号: 「" + titleName + "」を設定しました！")
    } else if (index == getLayOut(1,6)) {
      new EventMenu(ryoServerAssist).openEventMenu(p)
    } else if (index == getLayOut(5,6)) {
      new PlayerTitleData(ryoServerAssist).resetSelectTitle(p.getUniqueId.toString)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号をリセットしました。")
    }
  }

}

package com.ryoserver.Quest.Event

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class BeforeEventsMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = _
  override var p: Player = _

  def openMenu(player: Player, page: Int): Unit = {
    p = player
    name = s"過去のイベント:$page"
    var invIndex = 0
    EventDataProvider.oldEventData.zipWithIndex.foreach { case ((eventName, playerData), index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"$WHITE$eventName", List(
          s"${WHITE}あなたの順位:${if (playerData.contains(p.getUniqueId)) playerData.toSeq.sortBy(_._2).reverse.toMap.keys.toList.indexOf(p.getUniqueId) else "参加していません。"}",
          s"${WHITE}貢献数:${if (playerData.contains(p.getUniqueId)) playerData(p.getUniqueId) else "参加していません。"}"
        ))
        invIndex += 1
      }
    }
    if (page == 1) {
      setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
    } else {
      setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに戻ります。", List(s"${GRAY}クリックで戻ります。"))
    }
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerMotion(motion)
    open()
  }

  private def motion(p: Player, index: Int): Unit = {
    val page = p.getOpenInventory.getTitle.replace("過去のイベント:", "").toInt
    if (getLayOut(1, 6) == index) {
      if (page == 1) {
        new RyoServerMenu1(ryoServerAssist).menu(p)
      } else {
        new BeforeEventsMenu(ryoServerAssist).openMenu(p, page - 1)
      }
    } else if (getLayOut(9, 6) == index) {
      new BeforeEventsMenu(ryoServerAssist).openMenu(p, page + 1)
    }
  }

}

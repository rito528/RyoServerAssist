package com.ryoserver.Quest.Event

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton, RyoServerMenu1}
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
        setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BOOK, s"$WHITE$eventName", List(
          s"${WHITE}あなたの順位:${if (playerData.contains(p.getUniqueId)) playerData.toSeq.sortBy(_._2).reverse.toMap.keys.toList.indexOf(p.getUniqueId) else "参加していません。"}",
          s"${WHITE}貢献数:${if (playerData.contains(p.getUniqueId)) playerData(p.getUniqueId) else "参加していません。"}"
        )))
        invIndex += 1
      }
    }
    if (page == 1) {
      setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backPage))
    } else {
      setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backPage))
    }
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    .setLeftClickMotion(nextPage))
    build(new BeforeEventsMenu(ryoServerAssist).openMenu(_,1))
    open()
  }

  private def backPage(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("過去のイベント:", "").toInt
    if (page == 1) {
      new RyoServerMenu1(ryoServerAssist).menu(p)
    } else {
      new BeforeEventsMenu(ryoServerAssist).openMenu(p, page - 1)
    }
  }

  private def nextPage(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("過去のイベント:", "").toInt
    new BeforeEventsMenu(ryoServerAssist).openMenu(p, page + 1)
  }


}

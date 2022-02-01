package com.ryoserver.Quest.Event

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.PlayerTitleData
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Material}

class EventTitleMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = "イベント称号"
  override var p: Player = _

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  def openEventTitleMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}イベントメニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backMenu))
    val eventGateway = new EventGateway
    if (eventGateway.getEventRankingTitles(player.getUniqueId.toString) != null) {
      eventGateway.getEventRankingTitles(player.getUniqueId.toString).zipWithIndex.foreach { case (title, index) =>
        setButton(MenuButton(getX(index), getY(index), Material.NAME_TAG, s"$RESET$title", List(s"${GRAY}クリックで設定します。"))
          .setLeftClickMotion(setTitle(_, index)))
      }
    }
    setButton(MenuButton(5, 6, Material.PAPER, s"${GREEN}称号の設定をリセットします。", List(s"${GRAY}クリックでリセットします。"))
      .setLeftClickMotion(resetTitle))
    build(new EventTitleMenu(ryoServerAssist).openEventTitleMenu)
    open()
  }

  private def backMenu(p: Player): Unit = {
    new EventMenu(ryoServerAssist).openEventMenu(p)
  }

  private def setTitle(p: Player, index: Int): Unit = {
    if (p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val titleName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName + ChatColor.RESET
      new PlayerTitleData().setSelectTitle(p.getUniqueId, titleName)
      new Name().updateName(p)
      p.sendMessage(s"${AQUA}称号: 「${RESET}" + titleName + s"$AQUA」を設定しました！")
    }
  }

  private def resetTitle(p: Player): Unit = {
    new PlayerTitleData().resetSelectTitle(p.getUniqueId)
    new Name().updateName(p)
    p.sendMessage(s"${AQUA}称号をリセットしました。")
  }

}

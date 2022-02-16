package com.ryoserver.Quest.Event.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Quest.Event.EventDataProvider
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

import scala.collection.immutable.ListMap


class BeforeEventsMenu(page: Int, ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, s"過去のイベント:$page")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeBeforeEventMenuButton(player, page, ryoServerAssist)
    val uuid = player.getUniqueId
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> nextPage
    ) ++ EventDataProvider.oldEventData.zipWithIndex.map { case ((eventName, playerData), index) =>
      index -> getButton(s"$WHITE$eventName", List(
        s"${WHITE}あなたの順位:${
          if (playerData.contains(compute.player.getUniqueId))
            ListMap(playerData.toSeq.sortWith(_._2 > _._2):_*).toIndexedSeq.indexWhere(_._1 == compute.player.getUniqueId) + 1
        else "参加していません。"}",
        s"${WHITE}貢献数:${if (playerData.contains(compute.player.getUniqueId)) playerData(compute.player.getUniqueId) else "参加していません。"}")
      )
    }
  }
}

private case class computeBeforeEventMenuButton(player: Player, page: Int, ryoServerAssist: RyoServerAssist) {
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}前のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      if (page == 1) {
        new RyoServerMenu1(ryoServerAssist).open(player)
      } else {
        new BeforeEventsMenu(page, ryoServerAssist).open(player)
      }
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      new BeforeEventsMenu(page + 1, ryoServerAssist).open(player)
    }
  )

  def getButton(title: String, lore: List[String]): Button = {
    Button(
      ItemStackBuilder
        .getDefault(Material.BOOK)
        .title(title)
        .lore(lore)
        .build()
    )
  }
}
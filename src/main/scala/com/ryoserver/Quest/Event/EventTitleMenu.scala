package com.ryoserver.Quest.Event

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.PlayerTitleData
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EventTitleMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "イベント称号")
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeEventTitleMenuButton(player, ryoServerAssist)
    val eventGateway = new EventGateway
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(5, 6) -> resetTitle
    ) ++ (if (eventGateway.getEventRankingTitles(compute.player.getUniqueId.toString) != null) {
      eventGateway.getEventRankingTitles(compute.player.getUniqueId.toString).zipWithIndex.map { case (title, index) =>
        index -> getTitleButton(title)
      }.toMap
    } else Map.empty)
  }

}

private case class computeEventTitleMenuButton(player: Player, ryoServerAssist: RyoServerAssist) {
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}イベントメニューに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      new EventMenu(ryoServerAssist).open(player)
    }
  )

  val resetTitle: Button = Button(
    ItemStackBuilder
      .getDefault(Material.PAPER)
      .title(s"${GREEN}称号の設定をリセットします。")
      .lore(List(s"${GRAY}クリックでリセットします。"))
      .build(),
    ButtonMotion { _ =>
      new PlayerTitleData().resetSelectTitle(player.getUniqueId)
      new Name().updateName(player)
      player.sendMessage(s"${AQUA}称号をリセットしました。")
    }
  )

  def getTitleButton(title: String): Button = {
    Button(
      ItemStackBuilder
        .getDefault(Material.NAME_TAG)
        .title(s"$RESET$title")
        .lore(List(s"${GRAY}クリックで設定します。"))
        .build(),
      ButtonMotion { _ =>
        new PlayerTitleData().setSelectTitle(player.getUniqueId, title)
        new Name().updateName(player)
        player.sendMessage(s"${AQUA}称号: 「$RESET$title$AQUA」を設定しました！")
      }
    )
  }
}

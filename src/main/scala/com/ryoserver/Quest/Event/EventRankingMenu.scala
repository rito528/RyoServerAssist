package com.ryoserver.Quest.Event

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton, MenuFrame, MenuOld, MenuSkull}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material, OfflinePlayer}

import java.util.UUID

class EventRankingMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6,"イベントランキング")
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeEventRankingButton(player,ryoServerAssist)
    val gateway = new EventGateway()
    import compute._
    Map(
      getLayOut(1,6) -> backMenu
    ) ++ (
      EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.zipWithIndex.map { case ((uuid, counter), index) =>
        if (index / 9 + 1 >= 6) return Map.empty
        val OfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid))
        (index -> getButton(OfflinePlayer, s"${AQUA}[${index + 1}位] " + OfflinePlayer.getName, List(
          if (gateway.eventInfo(gateway.holdingEvent()).eventType == "delivery") s"${WHITE}納品量: " + counter + "個"
          else if (gateway.eventInfo(gateway.holdingEvent()).eventType == "suppression") s"${WHITE}討伐数: " + counter + "体"
          else null).filterNot(_ == null))
        )
      }.toMap
    )
  }
}

private case class computeEventRankingButton(player: Player,ryoServerAssist: RyoServerAssist) {
  val backMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}イベントメニューに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      new EventMenu(ryoServerAssist).open(player)
    }
  )

  def getButton(offlinePlayer: OfflinePlayer,title: String,lore:List[String]): Button = {
    Button(
      Item.getPlayerSkull(offlinePlayer,title,lore)
    )
  }
}

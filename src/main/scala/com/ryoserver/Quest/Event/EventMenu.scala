package com.ryoserver.Quest.Event

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EventMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(3,"イベント")
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeEventMenuButton(player, ryoServerAssist)
    import compute._
    val eventGateway = new EventGateway()
    val holdingEvent = eventGateway.holdingEvent()
    val eventData = eventGateway.eventInfo(holdingEvent)
    Map(
      getLayOut(1,3) -> backPage,
      getLayOut(5,3) -> beforeEvent,
      getLayOut(9,3) -> eventTitle
    ) ++ (if (holdingEvent != null) Map(getLayOut(3,2) -> eventInfoButton) else Map(getLayOut(5,2) -> eventNotHeldInfo) ++
      (if (holdingEvent != null && eventData.eventType == "delivery") Map(getLayOut(5,2) -> deliveryButton) else Map.empty) ++
      (if (holdingEvent != null && eventData.eventType != "bonus") Map(getLayOut(7,2) -> rankingMenu) else Map.empty))
  }

}

private case class computeEventMenuButton(player: Player,ryoServerAssist: RyoServerAssist) {
  private implicit val plugin: RyoServerAssist = ryoServerAssist
  private lazy val eventGateway = new EventGateway()
  private lazy val holdingEvent = eventGateway.holdingEvent()
  private lazy val eventData = eventGateway.eventInfo(holdingEvent)

  lazy val eventInfoButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"${GREEN}イベント情報")
      .lore(List(
        s"${WHITE}イベント名: ${eventData.name}",
        s"${WHITE}終了日: ${eventData.end}",
        s"${WHITE}イベント種別: " +
          s"${
            if (eventData.eventType == "delivery") "納品イベント"
            else if (eventData.eventType == "suppression") "討伐イベント"
            else if (eventData.eventType == "bonus") "経験値ボーナスイベント"
          }",
        if (eventData.eventType == "bonus") s"${WHITE}経験値増加率: ${eventData.exp}倍" else null,
        if (eventData.item != null && eventData.eventType == "delivery") s"${WHITE}集めるアイテム: ${
          val material = Material.matchMaterial(eventData.item)
          Translate.materialNameToJapanese(material)
        }"
        else if (eventData.item != null && eventData.eventType == "suppression") s"${WHITE}討伐するMOB: ${
          val entity = getEntity(eventData.item)
          Translate.entityNameToJapanese(entity)
        }"
        else null,
        if (eventData.eventType != "bonus") {
          val ranking = EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.toMap.keys.toList.indexOf(player.getUniqueId.toString)
          if (ranking != -1) s"${WHITE}あなたの順位: " + (ranking + 1) + "位" else s"${WHITE}あなたの順位: 参加していません。"
        }
        else null,
        if (eventData.distribution != 0 && eventData.eventType != "bonus") s"${WHITE}区切り間で貰えるガチャ券: ${eventData.distribution}枚" else null,
        if (eventData.reward != 0) s"${WHITE}区切り: ${eventData.reward.toString}" else null,
        if (eventData.eventType == "delivery") s"${WHITE}1個あたりのEXP量: ${eventData.exp}"
        else if (eventData.eventType == "suppression") s"${WHITE}1体あたりのEXP量: ${eventData.exp}"
        else null,
        if (eventData.eventType == "delivery") s"${WHITE}現在集められた量: ${EventDataProvider.eventCounter}個"
        else if (eventData.eventType == "suppression") s"${WHITE}現在倒された量: ${EventDataProvider.eventCounter}体"
        else null
      ).filterNot(_ == null))
      .build()
    )

  val eventNotHeldInfo: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"${GREEN}イベント情報")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build()
  )

  val deliveryButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.HOPPER_MINECART)
      .title(s"${GREEN}納品する")
      .lore(List(s"${GRAY}クリックで納品インベントリを開きます。"))
      .build(),
    ButtonMotion{_ =>
      new EventDeliveryMenu().openMenu(player)
    }
  )

  val rankingMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.EXPERIENCE_BOTTLE)
      .title(s"${GREEN}ランキングを表示します。")
      .lore(List(s"${GRAY}クリックでランキングを表示します。"))
      .build(),
    ButtonMotion{_ =>
      new EventRankingMenu(ryoServerAssist).open(player)
    }
  )

  val beforeEvent: Button = Button(
    ItemStackBuilder
      .getDefault(Material.ENCHANTED_BOOK)
      .title(s"${GREEN}過去のイベント")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion{_ =>
      new BeforeEventsMenu(ryoServerAssist).openMenu(player, 1)
    }
  )

  val eventTitle: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NAME_TAG)
      .title(s"${GREEN}イベント称号を表示します。")
      .lore(List(s"${GRAY}クリックで表示します。"))
      .build(),
    ButtonMotion{_ =>
      new EventTitleMenu(ryoServerAssist).open(player)
    }
  )

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}メニューに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      new RyoServerMenu1(ryoServerAssist).open(player)
    }
  )

}

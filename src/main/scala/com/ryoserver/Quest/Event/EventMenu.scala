package com.ryoserver.Quest.Event

import com.ryoserver.Menu.{Menu, MenuButton, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EventMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 3
  override var name: String = "イベント"
  override var p: Player = _

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  def openEventMenu(player: Player): Unit = {
    p = player
    val eventGateway = new EventGateway()
    val holdingEvent = eventGateway.holdingEvent()
    if (holdingEvent != null) {
      val eventData = eventGateway.eventInfo(holdingEvent)
      setButton(MenuButton(3, 2, Material.BOOK, s"${YELLOW}イベント情報", List(
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
          val ranking = EventDataProvider.eventRanking.toSeq.sortBy(_._2).reverse.toMap.keys.toList.indexOf(p.getUniqueId.toString)
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
      ).filterNot(_ == null)))
      if (eventData.eventType == "delivery") {
        setButton(MenuButton(5, 2, Material.HOPPER_MINECART, s"${YELLOW}納品する", List(s"${GRAY}クリックで納品インベントリを開きます。"))
          .setLeftClickMotion(delivery))
      }
      if (eventData.eventType != "bonus") {
        setButton(MenuButton(7, 2, Material.EXPERIENCE_BOTTLE, s"${YELLOW}ランキングを表示します。", List(s"${GRAY}クリックでランキングを表示します。"))
          .setLeftClickMotion(rankingMenu))
      }
    } else {
      setButton(MenuButton(5, 2, Material.BOOK, s"${YELLOW}イベント情報", List(s"${GRAY}現在開催されていません。")))
    }
    setButton(MenuButton(1, 3, Material.MAGENTA_GLAZED_TERRACOTTA, s"${YELLOW}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backMenu))
    setButton(MenuButton(5, 3, Material.ENCHANTED_BOOK, s"${GREEN}過去のイベント", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(beforeEvent))
    setButton(MenuButton(9, 3, Material.NAME_TAG, "イベント称号を表示します。", List(s"${GRAY}クリックで表示します。"))
      .setLeftClickMotion(eventTitle))
    build(new EventMenu(ryoServerAssist).openEventMenu)
    open()
  }

  private def delivery(p: Player): Unit = {
    new EventDeliveryMenu().openMenu(p)
  }

  private def rankingMenu(p: Player): Unit = {
    new EventRankingMenu(ryoServerAssist).openRankingMenu(p)
  }

  private def backMenu(p: Player): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(p)
  }

  private def beforeEvent(p: Player): Unit = {
    new BeforeEventsMenu(ryoServerAssist).openMenu(p, 1)
  }

  private def eventTitle(p: Player): Unit = {
    new EventTitleMenu(ryoServerAssist).openEventTitleMenu(p)
  }

}

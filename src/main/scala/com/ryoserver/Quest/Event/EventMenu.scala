package com.ryoserver.Quest.Event

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.Quest.LoadQuests
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EventMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 3
  override var name: String = "イベント"
  override var p: Player = _

  def openEventMenu(player: Player): Unit = {
    p = player
    registerMotion(motion)
    val eventGateway = new EventGateway(ryoServerAssist)
    val holdingEvent = eventGateway.holdingEvent()
    if (holdingEvent != null) {
      val eventData = eventGateway.eventInfo(holdingEvent)
      setItem(3, 2, Material.BOOK, effect = false, s"${YELLOW}イベント情報", List(
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
          if (material.isItem) LoadQuests.langFile.get("block." + material.getKey.toString.replace(":", ".")).textValue()
          else if (material.isBlock) LoadQuests.langFile.get("item." + material.getKey.toString.replace(":", ".")).textValue()
        }"
        else if (eventData.item != null && eventData.eventType == "suppression") s"${WHITE}討伐するMOB: ${
          val entity = getEntity(eventData.item)
          LoadQuests.langFile.get("entity." + entity.getKey.toString.replace(":", ".")).textValue()
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
      ).filterNot(_ == null))
      if (eventData.eventType == "delivery") setItem(5, 2, Material.HOPPER_MINECART, effect = false, s"${YELLOW}納品する", List(s"${GRAY}クリックで納品インベントリを開きます。"))
      if (eventData.eventType != "bonus") setItem(7, 2, Material.EXPERIENCE_BOTTLE, effect = false, s"${YELLOW}ランキングを表示します。", List(s"${GRAY}クリックでランキングを表示します。"))
    } else {
      setItem(5, 2, Material.BOOK, effect = false, s"${YELLOW}イベント情報", List(s"${GRAY}現在開催されていません。"))
    }
    setItem(1, 3, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${YELLOW}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
    setItem(9, 3, Material.NAME_TAG, effect = false, "イベント称号を表示します。", List(s"${GRAY}クリックで表示します。"))
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    if (index == getLayOut(5, 2)) {
      new EventDeliveryMenu(ryoServerAssist).openMenu(p)
    } else if (index == getLayOut(7, 2)) {
      new EventRankingMenu(ryoServerAssist).openRankingMenu(p)
    } else if (index == getLayOut(1, 3)) {
      new RyoServerMenu1(ryoServerAssist).menu(p)
    } else if (index == getLayOut(9, 3)) {
      new EventTitleMenu(ryoServerAssist).openEventTitleMenu(p)
    }

  }

}

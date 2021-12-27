package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util
import scala.jdk.CollectionConverters._

class SelectQuestMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = _
  var p: Player = _

  def inventory(player: Player, page: Int): Unit = {
    p = player
    name = s"クエスト選択:$page"
    val selectedQuests: Array[String] = Array.empty[String]
    val playerLevel = new GetPlayerData().getPlayerLevel(p)
    val lottery = new LotteryQuest
    val canQuests = lottery.canQuests(playerLevel)
    var invIndex = 0
    canQuests.zipWithIndex.foreach { case (questName, index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        lottery.questName = questName
        lottery.loadQuestData()
        val questType = if (lottery.questType.equalsIgnoreCase("delivery")) "納品クエスト"
        else if (lottery.questType.equalsIgnoreCase("suppression")) "討伐クエスト"
        val questDetails: java.util.List[String] = new util.ArrayList[String]()
        questDetails.add(s"$WHITE【リスト】")
        if (questType == "納品クエスト") {
          lottery.items.forEach(i => {
            val material = Material.matchMaterial(i.split(":")(0))
            val itemStack = new ItemStack(material)
            var itemName = ""
            if (material.isBlock) itemName = s"block.${itemStack.getType.getKey.toString.replace(":", ".")}"
            else if (material.isItem) itemName = s"item.${itemStack.getType.getKey.toString.replace(":", ".")}"
            questDetails.add(s"$WHITE・${LoadQuests.langFile.get(itemName).textValue()}:${i.split(":")(1)}個")
          })
        } else if (questType == "討伐クエスト") {
          lottery.mobs.forEach(i => {
            val entity = getEntity(i.split(":")(0))
            questDetails.add(s"$WHITE・" + LoadQuests.langFile.get(s"entity.${entity.getKey.toString.replace(":", ".")}").textValue() +
              ":" + i.split(":")(1) + "体")
          })
        }
        questDetails.add(s"$WHITE【説明】")
        questDetails.add(s"${WHITE}このクエストを完了した際に得られる経験値量:${lottery.exp}")
        setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"[$questType]" + lottery.questName, questDetails.asScala.toList)
        invIndex += 1
      }
    }
    if (page == 1) setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    else setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    new QuestData(ryoServerAssist).saveQuest(p, selectedQuests)
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    if (getLayOut(1, 6) == index) {
      if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
      else new SelectQuestMenu(ryoServerAssist).inventory(p, page - 1)
    } else if (getLayOut(9, 6) == index) {
      new SelectQuestMenu(ryoServerAssist).inventory(p, page + 1)
    } else if (index <= getLayOut(9, 5) || p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
        .replace("[討伐クエスト]", "")
        .replace("[納品クエスト]", "")
      new QuestSelectMenuMotions(ryoServerAssist).Select(p, questName)
    }

  }

}

package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.{ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.ChatColor._

import java.util
import scala.jdk.CollectionConverters._

class SelectQuestMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  var name: String = "クエスト選択"
  val slot: Int = 3
  var p: Player = _

  def inventory(player: Player): Unit = {
    p = player
    val questData = new QuestData(ryoServerAssist)
    var selectedQuests: Array[String] = Array.empty[String]
    val playerLevel = new GetPlayerData().getPlayerLevel(p)
    for (i <- 1 to 7 by 2) {
      val data = questData.loadQuest(p)
      val lottery = new LotteryQuest()
      if (data.isEmpty) {
        var loop = true
        do {
          lottery.lottery(playerLevel)
          if (!selectedQuests.contains(lottery.questName)) {
            loop = false
            selectedQuests :+= lottery.questName
          }
        } while (loop)
      } else {
        lottery.questName = data((i - 1) / 2)
        lottery.loadQuestData()
        selectedQuests :+= lottery.questName
      }
      val questType = if (lottery.questType.equalsIgnoreCase("delivery")) "納品クエスト"
      else if (lottery.questType.equalsIgnoreCase("suppression")) "討伐クエスト"
      val questDetails: java.util.List[String] = new util.ArrayList[String]()
      questDetails.add(ChatColor.WHITE + "【リスト】")
      if (questType == "納品クエスト") {
        lottery.items.forEach(i => {
          val material = Material.matchMaterial(i.split(":")(0))
          val itemStack = new ItemStack(material)
          var itemName = ""
          if (material.isBlock) itemName = "block." + itemStack.getType.getKey.toString.replace(":", ".")
          else if (material.isItem) itemName = "item." + itemStack.getType.getKey.toString.replace(":", ".")
          questDetails.add(ChatColor.WHITE + "・" + LoadQuests.langFile.get(itemName).textValue() + ":" + i.split(":")(1) + "個")
        })
      } else if (questType == "討伐クエスト") {
        lottery.mobs.forEach(i => {
          val entity = getEntity(i.split(":")(0))
          questDetails.add(ChatColor.WHITE + "・" + LoadQuests.langFile.get("entity." + entity.getKey.toString.replace(":", ".")).textValue() +
            ":" + i.split(":")(1) + "体")
        })
      }
      questDetails.add(ChatColor.WHITE + "【説明】")
      questDetails.add(ChatColor.WHITE + "このクエストを完了した際に得られる経験値量:" + lottery.exp)
      setItem(i + 1, 1, Material.BOOK, effect = false, s"[$questType]" + lottery.questName, questDetails.asScala.toList)
    }
    setItem(5, 3, Material.NETHER_STAR, effect = false, s"${GREEN}クエスト更新", List(s"${GRAY}クリックでクエストを更新します。"))
    setItem(9, 3, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    new QuestData(ryoServerAssist).saveQuest(p, selectedQuests)
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    val motions = Map[Int, Player => Unit](
      getLayOut(2, 1) -> {
        new QuestSelectMenuMotions(ryoServerAssist).Select(_, 0)
      },
      getLayOut(4, 1) -> {
        new QuestSelectMenuMotions(ryoServerAssist).Select(_, 1)
      },
      getLayOut(6, 1) -> {
        new QuestSelectMenuMotions(ryoServerAssist).Select(_, 2)
      },
      getLayOut(8, 1) -> {
        new QuestSelectMenuMotions(ryoServerAssist).Select(_, 3)
      },
      getLayOut(5, 3) -> new QuestSelectMenuMotions(ryoServerAssist).resetQuest,
      getLayOut(9, 3) -> {
        new RyoServerMenu1(ryoServerAssist).menu(_)
      }
    )
    if (motions.contains(index)) motions(index)(p)
  }

}

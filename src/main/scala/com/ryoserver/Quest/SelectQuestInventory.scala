package com.ryoserver.Quest

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.Menu.{Menu, createMenu}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.{ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util
import scala.jdk.CollectionConverters._

class SelectQuestInventory(ryoServerAssist: RyoServerAssist) extends Menu {

  var name: String = "クエスト選択"
  val slot: Int = 3
  var p: Player = _

  def inventory(player: Player): Unit = {
    p = player
    val questData = new QuestData(ryoServerAssist)
    var selectedQuests: Array[String] = Array.empty[String]
    val playerLevel = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
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
          questDetails.add(ChatColor.WHITE + "・" + loadQuests.langFile.get(itemName).textValue() + ":" + i.split(":")(1) + "個")
        })
      } else if (questType == "討伐クエスト") {
        lottery.mobs.forEach(i => {
          val entity = getEntity(i.split(":")(0))
          questDetails.add(ChatColor.WHITE + "・" + loadQuests.langFile.get("entity." + entity.getKey.toString.replace(":", ".")).textValue() +
            ":" + i.split(":")(1) + "体")
        })
      }
      questDetails.add(ChatColor.WHITE + "【説明】")
      questDetails.add(ChatColor.WHITE + "このクエストを完了した際に得られる経験値量:" + lottery.exp)
      setItem(i + 1, 1, Material.BOOK, effect = false, s"[$questType]" + lottery.questName, questDetails.asScala.toList)
    }
    setItem(5, 3, Material.NETHER_STAR, effect = false, "クエスト更新", List("クリックでクエストを更新します。"))
    setItem(9, 3, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, "メニューに戻る", List("クリックでメニューに戻ります。"))
    new QuestData(ryoServerAssist).saveQuest(p, selectedQuests)
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    val motions = Map[Int, Player => Unit](
      getLayOut(2, 1) -> {
        new QuestSelectInventoryMotions(ryoServerAssist).Select(_, 0)
      },
      getLayOut(4, 1) -> {
        new QuestSelectInventoryMotions(ryoServerAssist).Select(_, 1)
      },
      getLayOut(6, 1) -> {
        new QuestSelectInventoryMotions(ryoServerAssist).Select(_, 2)
      },
      getLayOut(8, 1) -> {
        new QuestSelectInventoryMotions(ryoServerAssist).Select(_, 3)
      },
      getLayOut(5, 3) -> new QuestSelectInventoryMotions(ryoServerAssist).resetQuest,
      getLayOut(9, 3) -> {
        new createMenu(ryoServerAssist).menu(_, ryoServerAssist)
      }
    )
    if (motions.contains(index)) motions(index)(p)
  }

}

package com.ryoserver.Quest

import com.ryoserver.Inventory.Item
import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, ChatColor, Material}

import java.util

class QuestSelectInventory(ryoServerAssist: RyoServerAssist) {

  def selectInventory(p:Player): Unit = {
    var selectedQuests:Array[String] = Array.empty[String]
    val playerLevel = new getPlayerData(ryoServerAssist).getPlayerLevel(p)
    val inv = Bukkit.createInventory(null,27,"クエスト選択")
    for (i <- 1 to 7 by 2) {
      val data = new QuestData(ryoServerAssist).loadQuest(p)
      val lottery = new LotteryQuest(ryoServerAssist)
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
        lottery.questName = data((i - 1)/2)
        lottery.getQuest()
        selectedQuests :+= lottery.questName
        println(lottery.questName)
      }
      val questType = if (lottery.questType.equalsIgnoreCase("delivery")) "納品クエスト" else ""
      val questDetails: java.util.List[String] = new util.ArrayList[String]()
      questDetails.add(ChatColor.WHITE + "【アイテムリスト】")
      lottery.items.forEach(i => {
        val material = Material.matchMaterial(i.split(":")(0))
        val itemStack = new ItemStack(material)
        var itemName = ""
        if (material.isBlock) itemName = "block." + itemStack.getType.getKey.toString.replace(":", ".")
        else if (material.isItem) itemName = "item." + itemStack.getType.getKey.toString.replace(":", ".")
        questDetails.add(ChatColor.WHITE + "・" + loadQuests.langFile.get(itemName).textValue() + ":" + i.split(":")(1) + "個")
      })
      questDetails.add(ChatColor.WHITE + "【説明】")
      lottery.descriptions.forEach(description => questDetails.add(ChatColor.WHITE + "・" + description))
      questDetails.add(ChatColor.WHITE + "このクエストを完了した際に得られる経験値量:" + lottery.exp)
      inv.setItem(i, Item.getItem(Material.BOOK, s"[$questType]" + lottery.questName, questDetails))
    }
    p.openInventory(inv)
    new QuestData(ryoServerAssist).saveQuest(p,selectedQuests)
  }
}

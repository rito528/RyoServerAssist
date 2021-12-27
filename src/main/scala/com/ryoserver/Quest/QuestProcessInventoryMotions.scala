package com.ryoserver.Quest

import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}
import org.bukkit.{Material, Sound}

class QuestProcessInventoryMotions(ryoServerAssist: RyoServerAssist) {

  def delivery(p: Player, inv: Inventory): Unit = {
    val questData = new QuestData(ryoServerAssist)
    var remainingItems: Array[ItemStack] = Array.empty
    var invItems: Array[ItemStack] = Array.empty
    //クエスト終了に必要な残りの納品アイテムを取得する
    questData.getSelectedQuestRemaining(p).split(";").foreach(remainingItem => {
      val itemData = remainingItem.split(":")
      remainingItems :+= new ItemStack(Material.matchMaterial(itemData(0)), itemData(1).toInt)
    })
    //インベントリの中身をすべて取得
    inv.getContents.foreach(invItem => {
      invItems :+= invItem
    })
    //納品アイテムを更新
    remainingItems.foreach(remainingItem => {
      invItems.foreach(invItem => {
        if (invItem != null) {
          if (invItem.getType == remainingItem.getType && remainingItem.getAmount > 0) {
            remainingItems = remainingItems.filterNot(_ == remainingItem) //一旦削除
            val amount = remainingItem.getAmount - invItem.getAmount
            if (amount < 0) {
              inv.removeItem(remainingItem)
              remainingItem.setAmount(0)
            } else {
              inv.removeItem(invItem)
              remainingItem.setAmount(amount)
            }
            remainingItems :+= remainingItem
          }
        }
      })
    })

    //残りの数を設定
    var questDone = true
    var remainingItem_str = ""
    remainingItems.foreach(remainingItem => {
      remainingItem_str += remainingItem.getType.name() + ":" + remainingItem.getAmount + ";"
      if (remainingItem.getAmount != 0) questDone = false
    })
    questData.setSelectedQuestItemRemaining(p, remainingItem_str)

    if (questDone) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questData.questClear(p)
      new QuestMenu(ryoServerAssist).selectInventory(p)
      new GiveTitle(ryoServerAssist).questClearNumber(p)
      new GiveTitle(ryoServerAssist).continuousLoginAndQuestClearNumber(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
      new QuestMenu(ryoServerAssist).selectInventory(p)
    }
  }

  def deliveryFromNeoStack(p: Player, inv: Inventory): Unit = {
    val questData = new QuestData(ryoServerAssist)
    var remainingItems: Array[ItemStack] = Array.empty
    var invItems: Array[ItemStack] = Array.empty
    //クエスト終了に必要な残りの納品アイテムを取得する
    questData.getSelectedQuestRemaining(p).split(";").foreach(remainingItem => {
      val itemData = remainingItem.split(":")
      remainingItems :+= new ItemStack(Material.matchMaterial(itemData(0)), itemData(1).toInt)
    })
    //インベントリの中身をすべて取得
    inv.getContents.foreach(invItem => {
      invItems :+= invItem
    })

    //neoStackからアイテムを納品
    val neoStack = new NeoStackGateway(ryoServerAssist)
    remainingItems.foreach(item => {
      if (item.getAmount > 0) {
        val requiredAmount = item.getAmount //クエストを達成するのに必要なアイテムの数
        val removedAmount = neoStack.removeNeoStack(p, item, requiredAmount) //実際にneoStackから引き出された数
        remainingItems = remainingItems
          .filterNot(_ == item)
        if (requiredAmount == removedAmount) {
          item.setAmount(0)
        } else {
          item.setAmount(requiredAmount - removedAmount)
        }
        remainingItems :+= item
      }
    })

    //残りの数を設定
    var questDone = true
    var remainingItem_str = ""
    remainingItems.foreach(remainingItem => {
      remainingItem_str += remainingItem.getType.name() + ":" + remainingItem.getAmount + ";"
      if (remainingItem.getAmount != 0) questDone = false
    })
    questData.setSelectedQuestItemRemaining(p, remainingItem_str)

    if (questDone) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questData.questClear(p)
      new QuestMenu(ryoServerAssist).selectInventory(p)
      new GiveTitle(ryoServerAssist).questClearNumber(p)
      new GiveTitle(ryoServerAssist).continuousLoginAndQuestClearNumber(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
      new QuestMenu(ryoServerAssist).selectInventory(p)
    }
  }


  def questDestroy(p: Player): Unit = {
    val questData = new QuestData(ryoServerAssist)
    questData.resetQuest(p)
    new QuestMenu(ryoServerAssist).selectInventory(p)
    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
  }

}

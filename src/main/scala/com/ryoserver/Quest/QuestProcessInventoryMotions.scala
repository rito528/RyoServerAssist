package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.giveTitle
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Material, Sound}
import org.bukkit.inventory.{Inventory, ItemStack}

class QuestProcessInventoryMotions(ryoServerAssist: RyoServerAssist) {

  def delivery(p:Player,inv:Inventory): Unit = {
    val questData = new QuestData(ryoServerAssist)
    var remainingItems:Array[ItemStack] = Array.empty
    var invItems:Array[ItemStack] = Array.empty
    //クエスト終了に必要な残りの納品アイテムを取得する
    questData.getSelectedQuestRemaining(p).split(";").foreach(remainingItem => {
      val itemData = remainingItem.split(":")
      remainingItems :+= new ItemStack(Material.matchMaterial(itemData(0)),itemData(1).toInt)
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
    questData.setSelectedQuestItemRemaining(p,remainingItem_str)

    if (questDone) {
      p.sendMessage(ChatColor.AQUA + "おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,1,1)
      questData.questClear(p)
      new QuestInventory(ryoServerAssist).selectInventory(p)
      new giveTitle(ryoServerAssist).questClearNumber(p)
      new giveTitle(ryoServerAssist).continuousLoginAndQuestClearNumber(p)
    } else {
      p.sendMessage(ChatColor.AQUA + "納品しました。")
      new QuestInventory(ryoServerAssist).selectInventory(p)
    }
  }

  def questDestroy(p:Player): Unit = {
    val questData = new QuestData(ryoServerAssist)
    questData.resetQuest(p)
    new QuestInventory(ryoServerAssist).selectInventory(p)
    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
  }

}

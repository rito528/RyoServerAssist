package com.ryoserver.Quest

import com.ryoserver.Menu.createMenu
import com.ryoserver.RyoServerAssist
import org.bukkit.{ChatColor, Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack


class QuestSelectInventoryEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle == "クエスト選択") {
      e.setCancelled(true)
      val p = e.getWhoClicked.asInstanceOf[Player]
      val questData = new QuestData(ryoServerAssist)
      val questNames = questData.loadQuest(p)
      val lottery = new LotteryQuest(ryoServerAssist)
      e.getSlot match {
        case 1 =>
          lottery.questName = questNames(0)
          lottery.getQuest()
          questData.selectQuest(p, lottery)
        case 3 =>
          lottery.questName = questNames(1)
          lottery.getQuest()
          questData.selectQuest(p, lottery)
        case 5 =>
          lottery.questName = questNames(2)
          lottery.getQuest()
          questData.selectQuest(p, lottery)
        case 7 =>
          lottery.questName = questNames(3)
          lottery.getQuest()
          questData.selectQuest(p, lottery)
        case 22 =>
          questData.resetQuest(p)
          new QuestSelectInventory(ryoServerAssist).selectInventory(p)
          p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
        case 26 =>
          p.openInventory(createMenu.menu())
          p.playSound(p.getLocation(), Sound.BLOCK_WOOL_PLACE, 1, 1)
        case _ =>
      }
    } else if (e.getView.getTitle == "納品") {
      val index = e.getSlot
      val inv = e.getInventory
      val p = e.getWhoClicked.asInstanceOf[Player]
      val questData = new QuestData(ryoServerAssist)
      if (index == 50 || index == 51 || index == 53) e.setCancelled(true)
      index match {
        case 51 =>
          var remainingItems:Array[ItemStack] = Array.empty
          var invItems:Array[ItemStack] = Array.empty
          //クエスト終了に必要な残りの納品アイテムを取得する
          questData.getSelectedQuestMaterials(p).split(";").foreach(remainingItem => {
            val itemData = remainingItem.split(":")
            remainingItems :+= new ItemStack(Material.matchMaterial(itemData(0)),itemData(1).toInt)
          })
          //インベントリの中身をすべて取得
          inv.getContents.foreach(invItem => {
            invItems :+= invItem
          })
          //納品アイテムを更新
          remainingItems.foreach(remainingItem => {
            remainingItems = remainingItems.filterNot(_ == remainingItem) //一旦削除
            invItems.foreach(invItem => {
              if (invItem != null) {
                if (invItem.getType == remainingItem.getType && remainingItem.getAmount > 0) {
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

          //余ったアイテムをすべて返す
          inv.getContents.foreach(is=> {
            if (is != null && is.getItemMeta != inv.getItem(50).getItemMeta && is.getItemMeta != inv.getItem(51).getItemMeta
              && is.getItemMeta != inv.getItem(53).getItemMeta) {
              p.getWorld.dropItem(p.getLocation(),is)
            }
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
            questData.resetQuest(p)
            new QuestSelectInventory(ryoServerAssist).selectInventory(p)
          } else {
            questData.setSelectedQuestItemRemaining(p,remainingItems.mkString(";"))
            p.sendMessage(ChatColor.AQUA + "納品しました。")
            new QuestSelectInventory(ryoServerAssist).selectInventory(p)
          }
        case 53 =>
          questData.resetQuest(p)
          new QuestSelectInventory(ryoServerAssist).selectInventory(p)
          p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
        case _ =>
      }
    }

  }

}

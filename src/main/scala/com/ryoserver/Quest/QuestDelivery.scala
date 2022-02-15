package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.Menu.SelectQuestMenu
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}

import scala.jdk.CollectionConverters.CollectionHasAsScala

class QuestDelivery(ryoServerAssist: RyoServerAssist) {

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  def delivery(p: Player): Unit = {
    val inventory = p.getOpenInventory.getTopInventory
    val playerData = QuestPlayerData.getPlayerQuestContext(p.getUniqueId)
    val progress = playerData.progress
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    //クエスト完了に必要とされるアイテム数とインベントリ内のアイテム数を確認し、該当アイテムを削除
    progress.get.foreach { case (require, amount) =>
      val requireMaterial = require.material
      val hasItemAmount = inventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
      if (amount >= hasItemAmount) {
        QuestPlayerData.setQuestData(p.getUniqueId,playerData.setProgress(Option(Map(require -> (amount - hasItemAmount)))))
        inventory.removeItem(new ItemStack(requireMaterial, hasItemAmount))
      } else {
        QuestPlayerData.setQuestData(p.getUniqueId,playerData.setProgress(Option(Map(require -> 0))))
        inventory.removeItem(new ItemStack(requireMaterial, amount))
      }
    }
    questClearCheck(p, QuestPlayerData.getPlayerQuestContext(p.getUniqueId).progress.get)
  }

  private def questClearCheck(p: Player, progress: Map[MaterialOrEntityType, Int]): Unit = {
    val questGateway = new QuestGateway(p)
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questGateway.questClear()
      new SelectQuestMenu(ryoServerAssist,1,QuestPlayerData.getQuestSortData(p.getUniqueId)).open(p)
      new GiveTitle().questClearNumber(p)
      new GiveTitle().continuousLoginAndQuestClearNumber(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
      new SelectQuestMenu(ryoServerAssist,1,QuestPlayerData.getQuestSortData(p.getUniqueId)).open(p)
    }
  }

  def buttonItemRemove(p: Player, inv: Inventory): Unit = {
    List(
      getLayOut(1, 6),
      getLayOut(2, 6),
      getLayOut(9, 6),
      if (p.getQuestLevel >= 20) getLayOut(3, 6) else -1
    ).filterNot(_ == -1).foreach(index => inv.remove(inv.getItem(index)))
  }

  def deliveryFromNeoStack(p: Player): Unit = {
    val neoStackGateway = new NeoStackGateway()
    val playerData = QuestPlayerData.getPlayerQuestContext(p.getUniqueId)
    val progress = playerData.progress
    val inventory = p.getOpenInventory.getTopInventory
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    progress.get.foreach { case (require, amount) =>
      val removedAmount = neoStackGateway.removeNeoStack(p, new ItemStack(require.material, 1), amount)
      if (amount > removedAmount) {
        QuestPlayerData.setQuestData(p.getUniqueId,playerData.setProgress(Option(Map(require -> (amount - removedAmount)))))
      } else {
        QuestPlayerData.setQuestData(p.getUniqueId,playerData.setProgress(Option(Map(require -> 0))))
      }
    }
    questClearCheck(p, QuestPlayerData.getPlayerQuestContext(p.getUniqueId).progress.get)
  }

  def questDestroy(p: Player): Unit = {
    val questGateway = new QuestGateway(p)
    questGateway.questClear()
  }

}

package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.Menu.{DailyQuestProcessMenu, DailyQuestRewardMenu, SelectQuestMenu}
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
    setQuestProgress(p, progress)
    questClearCheck(p, QuestPlayerData.getPlayerQuestContext(p.getUniqueId).progress.get)
    new SelectQuestMenu(ryoServerAssist,1,QuestPlayerData.getQuestSortData(p.getUniqueId)).open(p)
  }

  def dailyQuestDelivery(p: Player): Unit = {
    val inventory = p.getOpenInventory.getTopInventory
    val playerData = QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId)
    val progress = playerData.progress
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    //クエスト完了に必要とされるアイテム数とインベントリ内のアイテム数を確認し、該当アイテムを削除
    setDailyQuestProgress(p, progress)
    dailyQuestClearCheck(p, QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId).progress.get)
  }

  private def setQuestProgress(p: Player,progress: Option[Map[MaterialOrEntityType,Int]]): Unit = {
    val inventory = p.getOpenInventory.getTopInventory
    progress.get.foreach { case (require, amount) =>
      val nowData = QuestPlayerData.getPlayerQuestContext(p.getUniqueId)
      val nowProgress = nowData.progress.get
      val requireMaterial = require.material
      val hasItemAmount = inventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
      if (amount >= hasItemAmount) {
        QuestPlayerData.setQuestData(p.getUniqueId,nowData.setProgress(Option(nowProgress ++ Map(require -> (amount - hasItemAmount)))))
        inventory.removeItem(new ItemStack(requireMaterial, hasItemAmount))
      } else {
        QuestPlayerData.setQuestData(p.getUniqueId,nowData.setProgress(Option(nowProgress ++ Map(require -> 0))))
        inventory.removeItem(new ItemStack(requireMaterial, amount))
      }
    }
  }

  private def setDailyQuestProgress(p: Player,progress: Option[Map[MaterialOrEntityType,Int]]): Unit = {
    val inventory = p.getOpenInventory.getTopInventory
    progress.get.foreach { case (require, amount) =>
      val nowData = QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId)
      val nowProgress = nowData.progress.get
      val requireMaterial = require.material
      val hasItemAmount = inventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
      if (amount >= hasItemAmount) {
        QuestPlayerData.setDailyQuestData(p.getUniqueId,nowData.setProgress(Option(nowProgress ++ Map(require -> (amount - hasItemAmount)))))
        inventory.removeItem(new ItemStack(requireMaterial, hasItemAmount))
      } else {
        QuestPlayerData.setDailyQuestData(p.getUniqueId,nowData.setProgress(Option(nowProgress ++ Map(require -> 0))))
        inventory.removeItem(new ItemStack(requireMaterial, amount))
      }
    }
  }



  private def dailyQuestClearCheck(p: Player, progress: Map[MaterialOrEntityType, Int]): Unit = {
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！デイリークエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      new GiveTitle().questClearNumber(p)
      new GiveTitle().continuousLoginAndQuestClearNumber(p)
      new DailyQuestRewardMenu(ryoServerAssist).open(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
      new DailyQuestProcessMenu(ryoServerAssist).open(p)
    }
  }

  def deliveryFromNeoStack(p: Player): Unit = {
    val playerData = QuestPlayerData.getPlayerQuestContext(p.getUniqueId)
    val progress = playerData.progress
    val inventory = p.getOpenInventory.getTopInventory
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    setProgressFromNeoStack(p, progress)
    questClearCheck(p, QuestPlayerData.getPlayerQuestContext(p.getUniqueId).progress.get)
    new SelectQuestMenu(ryoServerAssist,1,QuestPlayerData.getQuestSortData(p.getUniqueId)).open(p)
  }

  def deliveryFromNeoStackDaily(p: Player): Unit = {
    val playerData = QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId)
    val progress = playerData.progress
    val inventory = p.getOpenInventory.getTopInventory
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    setProgressFromNeoStack(p, progress)
    dailyQuestClearCheck(p, QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId).progress.get)
  }

  private def setProgressFromNeoStack(p: Player, progress: Option[Map[MaterialOrEntityType, Int]]): Unit = {
    val neoStackGateway = new NeoStackGateway()
    progress.get.foreach { case (require, amount) =>
      val removedAmount = neoStackGateway.removeNeoStack(p, new ItemStack(require.material, 1), amount)
      val nowData = QuestPlayerData.getPlayerDailyQuestContext(p.getUniqueId)
      if (amount > removedAmount) {
        QuestPlayerData.setDailyQuestData(p.getUniqueId,nowData.setProgress(Option(nowData.progress.get ++ Map(require -> (amount - removedAmount)))))
      } else {
        QuestPlayerData.setDailyQuestData(p.getUniqueId,nowData.setProgress(Option(nowData.progress.get ++ Map(require -> 0))))
      }
    }
  }

  def questDestroy(p: Player): Unit = {
    val questGateway = new QuestGateway(p)
    questGateway.questDestroy()
  }

  def dailyQuestDestroy(p: Player): Unit = {
    val questGateway = new QuestGateway(p)
    questGateway.dailyQuestDestroy()
  }

}

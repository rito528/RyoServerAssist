package com.ryoserver.Quest.QuestServices

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackService
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.{MaterialOrEntityType, PlayerQuestDataContext, QuestDataContext}
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor.AQUA
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}

import java.util.UUID
import scala.jdk.CollectionConverters.CollectionHasAsScala

trait QuestService {

  val questData: Set[QuestDataContext]
  val selectFunc: (UUID,PlayerQuestDataContext) => Unit
  val playerQuestDataContext: PlayerQuestDataContext
  val p: Player

  private lazy val uuid = p.getUniqueId
  private lazy val topInventory = p.getOpenInventory.getTopInventory

  def selectQuest(questName: String): Unit = {
    selectFunc(
      uuid,
      playerQuestDataContext
        .selectQuest(Option(questName))
        .setProgress(questData.filter(_.questName == questName).head.requireList)
    )
  }

  def getCanQuests: Set[QuestDataContext] = {
    val playerLevel = p.getRyoServerData.level
    questData.filter(data => data.minLevel <= playerLevel && data.maxLevel >= playerLevel)
  }

  def getSelectedQuest: Option[String] = {
    if (playerQuestDataContext.selectedQuest.getOrElse("") != "") playerQuestDataContext.selectedQuest
    else None
  }

  def getSelectedQuestData: QuestDataContext = {
    questData.filter(_.questName == playerQuestDataContext.selectedQuest.get).head
  }

  def questClear(exp: Double): Unit = {
    new UpdateLevel().addExp(exp,p)
    p.getRyoServerData.addQuestClearTimes(1).apply(p)
  }

  def questDestroy(): Unit = {
    selectFunc(
      uuid,
      playerQuestDataContext
        .selectQuest(None)
        .setProgress(Map.empty)
    )
  }

  def delivery(): Unit = {
    //ボタン用アイテムを削除
    buttonItemRemove(p, topInventory)
    //クエストの進行状況の設定と納品したアイテムの削除
    val progress = setQuestProgressAndItemRemove().progress.get
    questClearCheck(p, progress)
  }

  def deliveryFromNeoStack(): Unit = {
    val progress = setProgressFromNeoStack().progress.get
    questClearCheck(p, progress)
  }

  private def setProgressFromNeoStack(): PlayerQuestDataContext = {
    val neoStackService = new NeoStackService
    playerQuestDataContext.progress.get.foldLeft(playerQuestDataContext)((nowData, require) => {
      val removedAmount = neoStackService.removeItemAmount(uuid,new ItemStack(require._1.material, 1),require._2).getOrElse(0)
      val removeAmount = if (require._2 >= removedAmount) require._2 - removedAmount else 0
      val newProgressData = nowData.changeProgress(require._1,removeAmount)
      selectFunc(uuid,newProgressData)
      newProgressData
    })
  }

  def questClearCheck(p: Player, progress: Map[MaterialOrEntityType, Int],ratio: Double = 1.0): Unit = {
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questClear(questData.filter(_.questName == playerQuestDataContext.selectedQuest.get).head.exp * ratio)
      new GiveTitle().questClearNumber(p)
      new GiveTitle().continuousLoginAndQuestClearNumber(p)
      questDestroy()
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
    }
  }

  private def setQuestProgressAndItemRemove(): PlayerQuestDataContext = {
    playerQuestDataContext.progress.get
      .foldLeft(playerQuestDataContext)((nowData, require) => {
        val requireMaterial = require._1.material
        val amount = require._2
        val hasItemAmount = topInventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
        val removeAmount = {
          if (amount >= hasItemAmount) hasItemAmount
          else amount
        }
        val newProgressData = nowData.changeProgress(require._1,amount - removeAmount)
        selectFunc(uuid,newProgressData)
        topInventory.removeItem(new ItemStack(requireMaterial, removeAmount))
        newProgressData
      })
  }

  private def buttonItemRemove(p: Player, inv: Inventory): Unit = {
    List(
      getLayOut(1, 6),
      getLayOut(2, 6),
      getLayOut(9, 6),
      if (p.getRyoServerData.level >= 20) getLayOut(3, 6) else -1
    ).filterNot(_ == -1).foreach(index => inv.remove(inv.getItem(index)))
  }

}

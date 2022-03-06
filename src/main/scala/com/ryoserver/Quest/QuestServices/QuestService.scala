package com.ryoserver.Quest.QuestServices

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import com.ryoserver.Quest.{MaterialOrEntityType, PlayerQuestDataContext, QuestDataContext, QuestGateway}
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

  private val uuid = p.getUniqueId
  private val topInventory = p.getOpenInventory.getTopInventory

  def selectQuest(questName: String): Unit = {
    selectFunc(
      uuid,
      playerQuestDataContext
        .selectQuest(Option(questName))
        .setProgress(questData.filter(_.questName == questName).head.requireList)
    )
  }

  def questClear(exp: Double): Unit = {
    new UpdateLevel().addExp(exp,p)
    p.addQuestClearTimes()
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
    setQuestProgressAndItemRemove()
    questClearCheck(p, playerQuestDataContext.progress.get)
  }

  def deliveryFromNeoStack(): Unit = {
    setProgressFromNeoStack()
    questClearCheck(p, playerQuestDataContext.progress.get)
  }

  private def setProgressFromNeoStack(): Unit = {
    val neoStackGateway = new NeoStackGateway()
    playerQuestDataContext.progress.get.foreach { case (require, amount) =>
      val removedAmount = neoStackGateway.removeNeoStack(p, new ItemStack(require.material, 1), amount)
      val nowData = playerQuestDataContext
      if (amount >= removedAmount) {
        selectFunc(uuid,nowData.changeProgress(require, amount - removedAmount))
      } else {
        selectFunc(uuid,nowData.changeProgress(require,0))
      }
    }
  }

  private def questClearCheck(p: Player, progress: Map[MaterialOrEntityType, Int]): Unit = {
    val questGateway = new QuestGateway(p)
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questGateway.questClear()
      new GiveTitle().questClearNumber(p)
      new GiveTitle().continuousLoginAndQuestClearNumber(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
    }
  }

  private def setQuestProgressAndItemRemove(): Unit = {
    playerQuestDataContext.progress.get.foreach { case (require, amount) =>
      val nowData = playerQuestDataContext
      val requireMaterial = require.material
      val hasItemAmount = topInventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
      val removeAmount = if (amount >= hasItemAmount) {
        selectFunc(uuid,nowData.changeProgress(require, amount - hasItemAmount))
        hasItemAmount
      } else {
        selectFunc(uuid,nowData.changeProgress(require,0))
        amount
      }
      topInventory.removeItem(new ItemStack(requireMaterial, removeAmount))
    }
  }

  private def buttonItemRemove(p: Player, inv: Inventory): Unit = {
    List(
      getLayOut(1, 6),
      getLayOut(2, 6),
      getLayOut(9, 6),
      if (p.getQuestLevel >= 20) getLayOut(3, 6) else -1
    ).filterNot(_ == -1).foreach(index => inv.remove(inv.getItem(index)))
  }

}

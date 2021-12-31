package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.Data
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.{Inventory, ItemStack}
import org.bukkit.{Material, Sound}

import scala.jdk.CollectionConverters.CollectionHasAsScala

class QuestProcessInventoryMotions(ryoServerAssist: RyoServerAssist) {

  def delivery(p: Player): Unit = {
    val questGateway = new QuestGateway()
    val inventory = p.getOpenInventory.getTopInventory
    var progress = questGateway.getQuestProgress(p)
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    //クエスト完了に必要とされるアイテム数とインベントリ内のアイテム数を確認し、該当アイテムを削除
    progress.foreach { case (requireName, amount) =>
      val requireMaterial = Material.matchMaterial(requireName)
      val hasItemAmount = inventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
      if (amount >= hasItemAmount) {
        progress += (requireName -> (amount - hasItemAmount))
        inventory.removeItem(new ItemStack(requireMaterial, hasItemAmount))
      } else {
        progress += (requireName -> 0)
        inventory.removeItem(new ItemStack(requireMaterial, amount))
      }
    }
    questClearCheck(p, progress)
  }

  def deliveryFromNeoStack(p: Player): Unit = {
    val questGateway = new QuestGateway()
    val neoStackGateway = new NeoStackGateway(ryoServerAssist)
    var progress = questGateway.getQuestProgress(p)
    val inventory = p.getOpenInventory.getTopInventory
    //ボタン用アイテムを削除
    buttonItemRemove(p, inventory)
    progress.foreach { case (requireName, amount) =>
      val requireMaterial = Material.matchMaterial(requireName)
      val removedAmount = neoStackGateway.removeNeoStack(p, new ItemStack(requireMaterial, 1), amount)
      if (amount > removedAmount) {
        progress += (requireName -> (amount - removedAmount))
      } else {
        progress += (requireName -> 0)
      }
    }
    questClearCheck(p, progress)
  }

  private def questClearCheck(p: Player, progress: Map[String, Int]): Unit = {
    val questGateway = new QuestGateway
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      questGateway.questClear(p, ryoServerAssist)
      new QuestMenu(ryoServerAssist).selectInventory(p)
      new GiveTitle(ryoServerAssist).questClearNumber(p)
      new GiveTitle(ryoServerAssist).continuousLoginAndQuestClearNumber(p)
    } else {
      questGateway.setQuestProgress(p, progress)
      p.sendMessage(s"${AQUA}納品しました。")
      new QuestMenu(ryoServerAssist).selectInventory(p)
    }
  }

  def buttonItemRemove(p: Player, inv: Inventory): Unit = {
    List(
      getLayOut(1, 6),
      getLayOut(2, 6),
      getLayOut(9, 6),
      if (Data.playerData(p.getUniqueId).level >= 20) getLayOut(3, 6) else -1
    ).filterNot(_ == -1).foreach(index => inv.remove(inv.getItem(index)))
  }


  def questDestroy(p: Player): Unit = {
    val questData = new QuestGateway()
    questData.resetQuest(p)
    buttonItemRemove(p, p.getOpenInventory.getTopInventory)
    new QuestMenu(ryoServerAssist).selectInventory(p)
    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
  }

}

//package com.ryoserver.Quest
//
//import com.ryoserver.Menu.MenuLayout.getLayOut
//import com.ryoserver.NeoStack.NeoStackGateway
//import com.ryoserver.Player.PlayerManager.getPlayerData
//import com.ryoserver.Quest.Menu.DailyQuestRewardMenu
//import com.ryoserver.RyoServerAssist
//import com.ryoserver.Title.GiveTitle
//import org.bukkit.ChatColor.AQUA
//import org.bukkit.entity.Player
//import org.bukkit.inventory.{Inventory, ItemStack}
//import org.bukkit.{Material, Sound}
//
//import scala.jdk.CollectionConverters.CollectionHasAsScala
//
//class DailyQuestProcessMotions(ryoServerAssist: RyoServerAssist) {
//
//  private implicit val plugin: RyoServerAssist = ryoServerAssist
//
//  def delivery(p: Player): Unit = {
//    val questGateway = new QuestGateway()
//    val inventory = p.getOpenInventory.getTopInventory
//    var progress = questGateway.getQuestProgress(p)
//    //ボタン用アイテムを削除
//    buttonItemRemove(p, inventory)
//    //クエスト完了に必要とされるアイテム数とインベントリ内のアイテム数を確認し、該当アイテムを削除
//    progress.foreach { case (requireName, amount) =>
//      val requireMaterial = Material.matchMaterial(requireName)
//      val hasItemAmount = inventory.all(requireMaterial).values().asScala.map(is => is.getAmount).sum
//      if (amount >= hasItemAmount) {
//        progress += (requireName -> (amount - hasItemAmount))
//        inventory.removeItem(new ItemStack(requireMaterial, hasItemAmount))
//      } else {
//        progress += (requireName -> 0)
//        inventory.removeItem(new ItemStack(requireMaterial, amount))
//      }
//    }
//    questClearCheck(p, progress)
//  }
//
//  private def questClearCheck(p: Player, progress: Map[String, Int]): Unit = {
//    val questGateway = new QuestGateway
//    questGateway.setDailyQuestProgress(p, progress)
//    if (progress.forall { case (_, amount) => amount == 0 }) {
//      p.sendMessage(s"${AQUA}おめでとうございます！デイリークエストが完了しました！")
//      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
//      new DailyQuestRewardMenu(ryoServerAssist).open(p)
//      new GiveTitle().questClearNumber(p)
//      new GiveTitle().continuousLoginAndQuestClearNumber(p)
//    } else {
//      p.sendMessage(s"${AQUA}納品しました。")
//      new QuestMenu().selectDailyQuestMenu(p)
//    }
//  }
//
//  def deliveryFromNeoStack(p: Player): Unit = {
//    val questGateway = new QuestGateway()
//    val neoStackGateway = new NeoStackGateway()
//    var progress = questGateway.getQuestProgress(p)
//    val inventory = p.getOpenInventory.getTopInventory
//    //ボタン用アイテムを削除
//    buttonItemRemove(p, inventory)
//    progress.foreach { case (requireName, amount) =>
//      val requireMaterial = Material.matchMaterial(requireName)
//      val removedAmount = neoStackGateway.removeNeoStack(p, new ItemStack(requireMaterial, 1), amount)
//      if (amount > removedAmount) {
//        progress += (requireName -> (amount - removedAmount))
//      } else {
//        progress += (requireName -> 0)
//      }
//    }
//    questClearCheck(p, progress)
//  }
//
//  def questDestroy(p: Player): Unit = {
//    val questData = new QuestGateway()
//    questData.resetQuest(p)
//    buttonItemRemove(p, p.getOpenInventory.getTopInventory)
//    new QuestMenu().selectInventory(p)
//    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
//  }
//
//  def buttonItemRemove(p: Player, inv: Inventory): Unit = {
//    List(
//      getLayOut(1, 6),
//      getLayOut(2, 6),
//      getLayOut(9, 6),
//      if (p.getQuestLevel >= 20) getLayOut(3, 6) else -1
//    ).filterNot(_ == -1).foreach(index => inv.remove(inv.getItem(index)))
//  }
//
//}

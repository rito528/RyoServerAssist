//package com.ryoserver.Quest
//
//import com.ryoserver.RyoServerAssist
//import org.bukkit.Sound
//import org.bukkit.entity.Player
//
//class QuestSelectMenuMotions(ryoServerAssist: RyoServerAssist) {
//
//  private implicit val plugin: RyoServerAssist = ryoServerAssist
//
//  def Select(p: Player, questName: String): Unit = {
//    val questData = new QuestGateway()
//    questData.selectQuest(p, LoadQuests.loadedQuests.filter(_.questName == questName).head.questName)
//    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
//    new QuestMenu().selectInventory(p)
//  }
//
//  def selectDailyQuest(p: Player, questName: String): Unit = {
//    val questData = new QuestGateway()
//    questData.selectDailyQuest(p, LoadQuests.loadedDailyQuests.filter(_.questName == questName).head.questName)
//    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
//    new QuestMenu().selectDailyQuestMenu(p)
//  }
//
//  def resetQuest(p: Player): Unit = {
//    val questData = new QuestGateway()
//    questData.resetQuest(p)
//    new QuestMenu().selectInventory(p)
//    p.playSound(p.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
//  }
//
//}

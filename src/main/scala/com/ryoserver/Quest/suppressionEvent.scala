package com.ryoserver.Quest

import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.giveTitle
import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}

class suppressionEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onEntityDeath(e: EntityDeathEvent): Unit = {
    val entity = e.getEntityType
    if (e.getEntity.getKiller.isInstanceOf[Player]) {
      val p = e.getEntity.getKiller
      val questData = new QuestData(ryoServerAssist)
      val lottery = new LotteryQuest()
      var pause = false
      if (questData.getSelectedQuest(p) != null) {
        lottery.questName = questData.getSelectedQuest(p)
        lottery.loadQuestData()
        if (lottery.questType == "suppression") {
          val remaining = questData.getSelectedQuestRemaining(p)
          var data: Array[String] = Array.empty[String]
          remaining.split(";").foreach(questEntity => {
            data :+= questEntity
            if (new QuestSelectInventory(ryoServerAssist).getEntity(questEntity.split(":")(0)) == entity) {
              data = data.filterNot(_ == questEntity)
              var amount = questEntity.split(":")(1).toInt - 1
              if (amount < 0) amount = 0
              else if (amount % 5 == 0) pause = true
              data :+= questEntity.split(":")(0) + ":" + amount
            }
          })

          var questDone = true
          data.foreach(d => if (d.split(":")(1).toInt != 0) questDone = false)
          if (questDone) {
            p.sendMessage(ChatColor.AQUA + "おめでとうございます！クエストが完了しました！")
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
            questData.questClear(p)
            new giveTitle(ryoServerAssist).questClearNumber(p)
          } else {
            questData.setSelectedQuestItemRemaining(p, data.mkString(";"))
          }
          if (!questDone && pause) {
            p.sendMessage(ChatColor.AQUA + "選択されているクエストの一部が完了しました！")
            p.sendMessage("[達成まで]")
            data.foreach(e => {
              p.sendMessage(loadQuests.langFile.get("entity." + new QuestSelectInventory(ryoServerAssist)
                .getEntity(e.split(":")(0)).getKey.toString.replace(":",".")).textValue() + "->" + e.split(":")(1) + "体")
            })
          }

        }
      }
    }
  }

}

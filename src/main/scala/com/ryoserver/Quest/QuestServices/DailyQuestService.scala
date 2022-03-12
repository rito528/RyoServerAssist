package com.ryoserver.Quest.QuestServices

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Player.PlayerManager.setPlayerData
import com.ryoserver.Quest.Menu.{DailyQuestProcessMenu, DailyQuestRewardMenu}
import com.ryoserver.Quest._
import com.ryoserver.RyoServerAssist
import com.ryoserver.Title.GiveTitle
import org.bukkit.ChatColor.AQUA
import org.bukkit.Sound
import org.bukkit.entity.Player

import java.util.{Date, UUID}

class DailyQuestService(ryoServerAssist: RyoServerAssist,player: Player) extends QuestService {

  private val uuid = player.getUniqueId
  private val questPlayerData = new QuestPlayerData()

  override val questData: Set[QuestDataContext] = QuestData.loadedDailyQuestData
  override val selectFunc: (UUID, PlayerQuestDataContext) => Unit = questPlayerData.processQuestData.selectDailyQuest
  override val playerQuestDataContext: PlayerQuestDataContext = questPlayerData.getQuestData.getPlayerDailyQuestContext(uuid)
  override val p: Player = player

  override def questClearCheck(p: Player, progress: Map[MaterialOrEntityType, Int],ratio: Double = 1.0): Unit = {
    if (progress.forall { case (_, amount) => amount == 0 }) {
      p.sendMessage(s"${AQUA}おめでとうございます！クエストが完了しました！")
      p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
      new GiveTitle().questClearNumber(p)
      new GiveTitle().continuousLoginAndQuestClearNumber(p)
      new DailyQuestRewardMenu(ryoServerAssist).open(p)
    } else {
      p.sendMessage(s"${AQUA}納品しました。")
      new DailyQuestProcessMenu(ryoServerAssist).open(p)
    }
  }

  override def questClear(exp: Double): Unit = {
    new UpdateLevel().addExp(exp,p)
    new QuestPlayerData().processQuestData.changeLastDailyQuest(uuid,new Date())
    p.addQuestClearTimes()
    questDestroy()
  }

}

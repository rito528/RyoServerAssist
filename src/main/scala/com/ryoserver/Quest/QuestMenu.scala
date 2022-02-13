package com.ryoserver.Quest

import com.ryoserver.Quest.Menu.{DailyQuestProcessMenu, QuestProcessMenu, SelectDailyQuestMenu, SelectQuestMenu}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class QuestMenu(implicit ryoServerAssist: RyoServerAssist) {

  def selectInventory(player: Player): Unit = {
    val questGateway = new QuestGateway()
    try {
      if (questGateway.getSelectedQuest(player).isEmpty) {
        new SelectQuestMenu(ryoServerAssist, 1, QuestSortedData.getPlayerQuestSortData(player)).open(player)
        player.playSound(player.getLocation, Sound.ITEM_BOOK_PAGE_TURN, 1, 1)
      } else {
        new QuestProcessMenu(ryoServerAssist).open(player)
      }
    } catch {
      case _: NoSuchElementException => player.sendMessage(s"${RED}デイリークエストを選択中にクエストのメニューを開くことはできません！")
    }
  }

  def selectDailyQuestMenu(player: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
    val dateDiffTable = sql"SELECT DATEDIFF(LastDailyQuest, NOW()) <= -1 AS dailyQuest FROM Players WHERE UUID=${player.getUniqueId.toString}"
    if (dateDiffTable.getHeadData.nonEmpty) {
      val diff = dateDiffTable.map(rs => rs.boolean("dailyQuest")).headOption.apply().get
      if (diff) {
        val questGateway = new QuestGateway()
        try {
          if (questGateway.getSelectedDailyQuest(player).isEmpty) {
            new SelectDailyQuestMenu(ryoServerAssist, 1).open(player)
            player.playSound(player.getLocation, Sound.ITEM_BOOK_PAGE_TURN, 1, 1)
          } else {
            new DailyQuestProcessMenu(ryoServerAssist).open(player)
          }
        } catch {
          case e: NoSuchElementException =>
            e.printStackTrace()
            player.sendMessage(s"${RED}クエストを選択中にデイリークエストのメニューを開くことはできません！")
        }
      } else {
        player.sendMessage(s"${RED}デイリークエストは一日一回のみこなすことができます!")
      }
    }
  }

}

package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.QuestServices.DailyQuestService
import com.ryoserver.Quest.{QuestDataContext, QuestPlayerData, QuestType}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.apache.commons.lang.time.DateUtils
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

import java.util.{Calendar, Date}

class SelectDailyQuestMenu(ryoServerAssist: RyoServerAssist, page: Int) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, s"デイリークエスト選択:$page")

  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    val lastDate = player.getRyoServerData.lastDailyQuestDate
    val now = new Date()
    if (DateUtils.truncate(lastDate,Calendar.DAY_OF_MONTH).getTime != DateUtils.truncate(now,Calendar.DAY_OF_MONTH).getTime) {
      player.playSound(player.getLocation, Sound.ITEM_BOOK_PAGE_TURN, 1, 1)
      val questService = new DailyQuestService(ryoServerAssist,player)
      questService.getSelectedQuest match {
        case Some(_) =>
          new DailyQuestProcessMenu(ryoServerAssist).open(player)
          false
        case None =>
          true
      }
    } else {
      player.sendMessage(s"${RED}デイリークエストは1日1回のみこなすことができます。")
      false
    }
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questService = new DailyQuestService(ryoServerAssist,player)
    val compute = computeSelectDailyQuestButton(player, page, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> nextPage
    ) ++ questService.getCanQuests.zipWithIndex.filter { case (_, index) =>
      index < (getLayOut(9, 5) + 1) * this.page && (getLayOut(9, 5) + 1) * (this.page - 1) <= index
    }.map { case (questData, index) => index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> getQuestButton(questData) }.toMap
  }

}

private case class computeSelectDailyQuestButton(player: Player, page: Int, ryoServerAssist: RyoServerAssist) {
  private val questService = new DailyQuestService(ryoServerAssist,player)

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(if (page == 1) s"${GREEN}メニューに戻る" else s"${GREEN}前のページに移動します。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      if (page == 1)
        new RyoServerMenu1(ryoServerAssist).open(player)
      else
        new SelectDailyQuestMenu(ryoServerAssist, page - 1).open(player)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      new SelectDailyQuestMenu(ryoServerAssist, page + 1).open(player)
    }
  )

  def getQuestButton(questData: QuestDataContext): Button = {
    val description = List(
      "",
      s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
    )
    if (questData.questType == QuestType.delivery) {
      val requireList = questData.requireList.map { case (require, amount) =>
        s"$WHITE${Translate.materialNameToJapanese(require.material)}:${amount}個"
      }
      return Button(
        ItemStackBuilder
          .getDefault(Material.BOOK)
          .title(s"$RESET[納品クエスト]${questData.questName}")
          .lore(List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ description)
          .build(),
        ButtonMotion { _ =>
          questService.selectQuest(questData.questName)
          new DailyQuestProcessMenu(ryoServerAssist).open(player)
        }
      )
    } else if (questData.questType == QuestType.suppression) {
      val requireList = questData.requireList.map { case (require, amount) =>
        s"$WHITE${Translate.entityNameToJapanese(require.entityType)}:${amount}体"
      }
      return Button(
        ItemStackBuilder
          .getDefault(Material.BOOK)
          .title(s"$RESET[討伐クエスト]${questData.questName}")
          .lore(List(
            s"$WHITE【討伐リスト】"
          ) ++ requireList ++ description)
          .build(),
        ButtonMotion { _ =>
          questService.selectQuest(questData.questName)
          new DailyQuestProcessMenu(ryoServerAssist).open(player)
        }
      )
    }
    null
  }
}

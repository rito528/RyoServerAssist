package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Quest.QuestServices.NormalQuestService
import com.ryoserver.Quest._
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class SelectQuestMenu(ryoServerAssist: RyoServerAssist, page: Int, sortType: QuestSortContext) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, s"クエスト選択:$page")

  /*
    このメニューはクエストが選択されていなかった場合に開き、選択されていた場合はProcessMenuに進む
   */
  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    player.playSound(player.getLocation,Sound.ITEM_BOOK_PAGE_TURN,1,1)
    val questService = new NormalQuestService(player)
    questService.getSelectedQuest match {
      case Some(_) =>
        new QuestProcessMenu(ryoServerAssist).open(player)
        false
      case None =>
        true
    }
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questService = new NormalQuestService(player)
    val compute = computeSelectQuestButton(player, page, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(5, 6) -> sort,
      getLayOut(9, 6) -> nextPage
    ) ++ questService.getQuests(sortType).zipWithIndex.filter { case (_, index) =>
      index < (getLayOut(9, 5) + 1) * this.page && (getLayOut(9, 5) + 1) * (this.page - 1) <= index
    }.map { case (questData, index) => (index - ((getLayOut(9, 5) + 1) * (this.page - 1))) -> getQuestButton(questData) }.toMap
  }

}

private case class computeSelectQuestButton(player: Player, page: Int, ryoServerAssist: RyoServerAssist) {
  private lazy val questService = new NormalQuestService(player)

  private lazy val nowSortType: QuestSortContext = new QuestPlayerData().getQuestData.getQuestSortData(player.getUniqueId)

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
        new SelectQuestMenu(ryoServerAssist, page - 1, nowSortType).open(player)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      new SelectQuestMenu(ryoServerAssist, page + 1, nowSortType).open(player)
    }
  )

  val sort: Button = Button(
    ItemStackBuilder
      .getDefault(Material.STONECUTTER)
      .title(s"${GREEN}クエストのソートを行います。")
      .lore(List(
        s"${WHITE}現在の表示順:$GREEN${nowSortType.name}",
        s"${GRAY}クリックで変更します。")
      )
      .build(),
    ButtonMotion { _ =>
      val nextType = QuestSortTypeDependency.dependency(nowSortType)
      questService.setQuestSortData(nextType)
      new SelectQuestMenu(ryoServerAssist, page, nextType).open(player)
    }
  )

  def getQuestButton(questData: QuestDataContext): Button = {
    val description = List(
      "",
      s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
      "",
      s"${WHITE}左クリックでクエスト選択",
      s"${WHITE}右クリックでブックマークに登録・解除します"
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
        ButtonMotion { e =>
          val questName = questData.questName
          e.getClick match {
            case ClickType.RIGHT =>
              if (questService.setBookmark(questName)) {
                player.sendMessage(s"$AQUA${questName}をブックマークに追加しました！")
              } else {
                player.sendMessage(s"$RED${questName}をブックマークから削除しました。")
              }
            case ClickType.LEFT =>
              questService.selectQuest(questName)
              new QuestProcessMenu(ryoServerAssist).open(player)
            case _ =>
          }
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
        ButtonMotion { e =>
          val questName = questData.questName
          e.getClick match {
            case ClickType.RIGHT =>
              if (questService.setBookmark(questName)) {
                player.sendMessage(s"$AQUA${questName}をブックマークに追加しました！")
              } else {
                player.sendMessage(s"$RED${questName}をブックマークから削除しました。")
              }
            case ClickType.LEFT =>
              questService.selectQuest(questName)
              new QuestProcessMenu(ryoServerAssist).open(player)
            case _ =>
          }
        }
      )
    }
    null
  }
}

package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.{QuestGateway, QuestSelectMenuMotions, QuestType}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectDailyQuestMenu(ryoServerAssist: RyoServerAssist, page: Int) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, s"デイリークエスト選択:$page")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questGateway = new QuestGateway
    val playerLevel = player.getQuestLevel
    val compute = computeSelectDailyQuestButton(player, page, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> nextPage
    ) ++ questGateway.getCanDailyQuests(playerLevel).zipWithIndex.filter { case (_, index) =>
      index < (getLayOut(9, 5) + 1) * this.page && (getLayOut(9, 5) + 1) * (this.page - 1) <= index
    }.map { case (questData, index) => index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> getQuestButton(questData) }.toMap
  }

}

private case class computeSelectDailyQuestButton(player: Player, page: Int, ryoServerAssist: RyoServerAssist) {
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

  def getQuestButton(questData: QuestType): Button = {
    val description = List(
      "",
      s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
    )
    if (questData.questType == "delivery") {
      val requireList = questData.requireList.map { case (require, amount) =>
        s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
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
          new QuestSelectMenuMotions(ryoServerAssist).selectDailyQuest(player, questData.questName)
        }
      )
    } else if (questData.questType == "suppression") {
      val requireList = questData.requireList.map { case (require, amount) =>
        s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
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
          new QuestSelectMenuMotions(ryoServerAssist).selectDailyQuest(player, questData.questName)
        }
      )
    }
    null
  }
}

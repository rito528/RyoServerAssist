package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{MenuOld, MenuButton}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectQuestMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  val slot: Int = 6
  var name: String = _
  var p: Player = _

  def inventory(player: Player, page: Int, sortType: QuestSortType): Unit = {
    p = player
    name = s"クエスト選択:$page"
    val playerLevel = p.getQuestLevel
    val questGateway = new QuestGateway
    sortType match {
      case QuestSortType.normal =>
        setSelectQuestItem(questGateway.getCanQuests(playerLevel), page)
      case QuestSortType.neoStack =>
        setSelectQuestItem(questGateway.nowNeoStackCanQuest(p), page)
      case QuestSortType.bookMark =>
        setSelectQuestItem(questGateway.getBookmarkCanQuest(p), page)
    }
    if (page == 1) setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
      .setLeftClickMotion(backPage))
    else setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(backPage))

    setButton(MenuButton(5, 6, Material.STONECUTTER, s"${GREEN}クエストのソートを行います。"
      , List(s"${WHITE}現在の表示順:$GREEN${QuestSortedData.getPlayerQuestSortData(p).name}",
        s"${GRAY}クリックで変更します。"))
      .setLeftClickMotion(sort)
      .setReload())
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(nextPage))
    build(new SelectQuestMenu(ryoServerAssist).inventory(_, page, sortType))
    open()
  }

  private def setSelectQuestItem(showQuests: List[QuestType], page: Int): Unit = {
    var invIndex = 0
    showQuests.zipWithIndex.foreach { case (questData, index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        val description = List(
          "",
          s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
          "",
          s"${WHITE}左クリックでクエスト選択",
          s"${WHITE}右クリックでブックマークに登録・解除します"
        )
        if (questData.questType == "delivery") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
          }
          setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BOOK, s"$RESET[納品クエスト]${questData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ description)
            .setLeftClickMotion(selectQuest(_, index - ((getLayOut(9, 5) + 1) * (page - 1))))
            .setRightClickMotion(bookmark(_, index - ((getLayOut(9, 5) + 1) * (page - 1)))))
        } else if (questData.questType == "suppression") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
          }
          setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BOOK, s"$RESET[討伐クエスト]${questData.questName}", List(
            s"$WHITE【討伐リスト】"
          ) ++ requireList ++ description)
            .setLeftClickMotion(selectQuest(_, index - ((getLayOut(9, 5) + 1) * (page - 1))))
            .setRightClickMotion(bookmark(_, index - ((getLayOut(9, 5) + 1) * (page - 1)))))
        }
        invIndex += 1
      }
    }
  }

  private def backPage(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
    else new SelectQuestMenu(ryoServerAssist).inventory(p, page - 1, QuestSortedData.getPlayerQuestSortData(p))
  }

  private def sort(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    val nextType = QuestSortTypeDependency.dependency(QuestSortedData.getPlayerQuestSortData(p))
    QuestSortedData.setPlayerQuestSortData(p, nextType)
    new SelectQuestMenu(ryoServerAssist).inventory(p, page, nextType)
  }

  private def nextPage(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    new SelectQuestMenu(ryoServerAssist).inventory(p, page + 1, QuestSortedData.getPlayerQuestSortData(p))
  }

  private def selectQuest(p: Player, index: Int): Unit = {
    val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
      .replace("[討伐クエスト]", "")
      .replace("[納品クエスト]", "")
    new QuestSelectMenuMotions(ryoServerAssist).Select(p, questName)
  }

  private def bookmark(p: Player, index: Int): Unit = {
    val gateway = new QuestGateway
    val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
      .replace("[討伐クエスト]", "")
      .replace("[納品クエスト]", "")
    if (gateway.setBookmark(p, questName)) {
      p.sendMessage(s"$AQUA${questName}をブックマークに追加しました！")
    } else {
      p.sendMessage(s"$RED${questName}をブックマークから削除しました。")
    }
  }

}

package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectDailyQuestMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = _
  override var p: Player = _

  def inventory(player: Player, page: Int): Unit = {
    p = player
    name = s"デイリークエスト選択:$page"
    val playerLevel = p.getQuestLevel
    val questGateway = new QuestGateway
    setSelectQuestItem(questGateway.getCanDailyQuests(playerLevel), page)
    if (page == 1) setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
      .setLeftClickMotion(back))
    else setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(back))
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(next))
    build(new SelectDailyQuestMenu(ryoServerAssist).inventory(_, 1))
    open()
  }

  def setSelectQuestItem(showQuests: List[QuestType], page: Int): Unit = {
    var invIndex = 0
    showQuests.zipWithIndex.foreach { case (questData, index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        val description = List(
          "",
          s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
        )
        if (questData.questType == "delivery") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
          }
          setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BOOK, s"$RESET[納品クエスト]${questData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ description)
            .setLeftClickMotion(selectQuest(_, index - ((getLayOut(9, 5) + 1) * (page - 1)))))
        } else if (questData.questType == "suppression") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
          }
          setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BOOK, s"$RESET[討伐クエスト]${questData.questName}", List(
            s"$WHITE【討伐リスト】"
          ) ++ requireList ++ description)
            .setLeftClickMotion(selectQuest(_, index - ((getLayOut(9, 5) + 1) * (page - 1)))))
        }
        invIndex += 1
      }
    }
  }

  private def back(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("デイリークエスト選択:", "").toInt
    if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
    else new SelectDailyQuestMenu(ryoServerAssist).inventory(p, page - 1)
  }

  private def next(p: Player): Unit = {
    val page = p.getOpenInventory.getTitle.replace("デイリークエスト選択:", "").toInt
    new SelectDailyQuestMenu(ryoServerAssist).inventory(p, page + 1)
  }

  private def selectQuest(p: Player, index: Int): Unit = {
    val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
      .replace("[討伐クエスト]", "")
      .replace("[納品クエスト]", "")
    new QuestSelectMenuMotions(ryoServerAssist).selectDailyQuest(p, questName)
  }

}

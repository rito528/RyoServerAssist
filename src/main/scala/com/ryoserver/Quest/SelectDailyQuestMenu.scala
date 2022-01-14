package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
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
    setSelectQuestItem(questGateway.getCanDailyQuests(playerLevel),page)
    if (page == 1) setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    else setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerMotion(motion)
    open()
  }

  def setSelectQuestItem(showQuests: List[QuestType], page: Int): Unit = {
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
          setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"$RESET[納品クエスト]${questData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ description)
        } else if (questData.questType == "suppression") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
          }
          setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"$RESET[討伐クエスト]${questData.questName}", List(
            s"$WHITE【討伐リスト】"
          ) ++ requireList ++ description)
        }
        invIndex += 1
      }
    }
  }

  def motion(p: Player, index: Int): Unit = {
    val page = p.getOpenInventory.getTitle.replace("デイリークエスト選択:", "").toInt
    if (getLayOut(1, 6) == index) {
      if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
      else new SelectDailyQuestMenu(ryoServerAssist).inventory(p, page - 1)
    } else if (getLayOut(9, 6) == index) {
      new SelectDailyQuestMenu(ryoServerAssist).inventory(p, page + 1)
    } else if (index <= getLayOut(9, 5) || p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
        .replace("[討伐クエスト]", "")
        .replace("[納品クエスト]", "")
      new QuestSelectMenuMotions(ryoServerAssist).selectDailyQuest(p, questName)
    }

  }

}

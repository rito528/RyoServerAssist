package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuData, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectQuestMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = _
  var p: Player = _

  def inventory(player: Player, page: Int,sortType:QuestSortType): Unit = {
    p = player
    name = s"クエスト選択:$page"
    val playerLevel = new GetPlayerData().getPlayerLevel(p)
    val questGateway = new QuestGateway
    sortType match {
      case QuestSortType.normal =>
        setSelectQuestItem(questGateway.getCanQuests(playerLevel),1)
      case QuestSortType.neoStack =>
        setSelectQuestItem(questGateway.nowNeoStackCanQuest(p,ryoServerAssist),1)
      case QuestSortType.bookMark =>
        setSelectQuestItem(questGateway.getBookmarkCanQuest(p),1)
    }
    if (page == 1) setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    else setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(5,6,Material.STONECUTTER,effect = false,s"${GREEN}クエストのソートを行います。"
      ,List(s"${WHITE}現在の表示順:$GREEN${QuestSortedData.getPlayerQuestSortData(p).name}",
      s"${GRAY}クリックで変更します。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerNeedClickMotion(motion)
    open()
  }

  def registerNeedClickMotion(func: (Player, Int, Boolean) => Unit): Unit = {
    MenuData.dataNeedClick += (name -> func)
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
  }

  def setSelectQuestItem(showQuests:List[QuestType],page:Int): Unit = {
    var invIndex = 0
    showQuests.zipWithIndex.foreach { case (questData, index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        val description = List(
          "",
          s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}",
          "",
          s"${WHITE}左クリックでクエスト選択",
          s"${WHITE}右クリックでブックマークに登録します"
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

  def motion(p: Player, index: Int, isRightClick: Boolean): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    if (getLayOut(1, 6) == index) {
      if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
      else new SelectQuestMenu(ryoServerAssist).inventory(p, page - 1,QuestSortedData.getPlayerQuestSortData(p))
    } else if (getLayOut(9, 6) == index) {
      new SelectQuestMenu(ryoServerAssist).inventory(p, page + 1, QuestSortedData.getPlayerQuestSortData(p))
    } else if (getLayOut(5, 6) == index) {
      val nextType = QuestSortTypeDependency.dependency(QuestSortedData.getPlayerQuestSortData(p))
      QuestSortedData.setPlayerQuestSortData(p,nextType)
      new SelectQuestMenu(ryoServerAssist).inventory(p, page,nextType)
    } else if (index <= getLayOut(9, 5) || p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
        .replace("[討伐クエスト]", "")
        .replace("[納品クエスト]", "")
      if (!isRightClick) {
        new QuestSelectMenuMotions(ryoServerAssist).Select(p, questName)
      } else {
        val gateway = new QuestGateway
        if (gateway.setBookmark(p, questName)) {
          p.sendMessage(s"$AQUA${questName}をブックマークに追加しました！")
        } else {
          p.sendMessage(s"$RED${questName}をブックマークから削除しました。")
        }
      }
    }

  }

}

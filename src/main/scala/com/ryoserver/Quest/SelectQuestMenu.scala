package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
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

  def inventory(player: Player, page: Int): Unit = {
    p = player
    name = s"クエスト選択:$page"
    val playerLevel = new GetPlayerData().getPlayerLevel(p)
    val questGateway = new QuestGateway
    val canQuests = questGateway.getCanQuests(playerLevel)
    var invIndex = 0
    canQuests.zipWithIndex.foreach { case (questData, index) =>
      if (index < (getLayOut(9, 5) + 1) * page && (getLayOut(9, 5) + 1) * (page - 1) <= index) {
        if (questData.questType == "delivery") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
          }
          setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"$RESET[納品クエスト]${questData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}"
          ))
        } else if (questData.questType == "suppression") {
          val requireList = questData.requireList.map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
          }
          setItem(getX(invIndex), getY(invIndex), Material.BOOK, effect = false, s"$RESET[討伐クエスト]${questData.questName}", List(
            s"$WHITE【討伐リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストをクリアした際に得られる経験値量:${questData.exp}"
          ))
        }
        invIndex += 1
      }
    }
    if (page == 1) setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    else setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    val page = p.getOpenInventory.getTitle.replace("クエスト選択:", "").toInt
    if (getLayOut(1, 6) == index) {
      if (page == 1) new RyoServerMenu1(ryoServerAssist).menu(p)
      else new SelectQuestMenu(ryoServerAssist).inventory(p, page - 1)
    } else if (getLayOut(9, 6) == index) {
      new SelectQuestMenu(ryoServerAssist).inventory(p, page + 1)
    } else if (index <= getLayOut(9, 5) || p.getOpenInventory.getTopInventory.getItem(index) != null) {
      val questName = p.getOpenInventory.getTopInventory.getItem(index).getItemMeta.getDisplayName
        .replace("[討伐クエスト]", "")
        .replace("[納品クエスト]", "")
      new QuestSelectMenuMotions(ryoServerAssist).Select(p, questName)
    }

  }

}

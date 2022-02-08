package com.ryoserver.Quest

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{MenuOld, MenuButton}
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestProcessMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  val slot: Int = 6
  var p: Player = _
  var name: String = _

  partButton = true

  def inventory(player: Player): Unit = {
    p = player
    val questGateway = new QuestGateway()
    questGateway.getSelectedQuest(p) match {
      case Some(selectedQuestData) =>
        if (selectedQuestData.questType == "delivery") {
          name = "納品"
          val requireList = questGateway.getQuestProgress(p).map { case (require, amount) =>
            s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
          }
          setButton(MenuButton(1, 6, Material.BOOK, s"$RESET[納品クエスト]${selectedQuestData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストを完了した際に得られる経験値量:${selectedQuestData.exp}"
          )))
          setButton(MenuButton(2, 6, Material.NETHER_STAR, s"${YELLOW}納品する", List(s"${GRAY}クリックで納品します。"))
            .setLeftClickMotion(new QuestProcessInventoryMotions(ryoServerAssist).delivery)
            .setReload())
          if (p.getQuestLevel >= 20) {
            val neoStackGateway = new NeoStackGateway()
            setButton(MenuButton(3, 6, Material.SHULKER_BOX, s"${YELLOW}neoStackから納品します。",
              List(s"${GRAY}クリックでneoStackから納品します。") ++ questGateway.getQuestProgress(p).map { case (require, amount) =>
                s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${
                  val neoStackAmount = neoStackGateway.getNeoStackAmount(p, new ItemStack(Material.matchMaterial(require)))
                  if (neoStackAmount >= amount) s"$AQUA$BOLD${UNDERLINE}OK (所持数:${neoStackAmount}個)"
                  else s"$RED$BOLD${UNDERLINE}${-(neoStackAmount - amount)}個不足しています"
                }"
              }
            ).setLeftClickMotion(new QuestProcessInventoryMotions(ryoServerAssist).deliveryFromNeoStack)
              .setReload())
            buttons :+= getLayOut(3, 6)
          }
        } else if (selectedQuestData.questType == "suppression") {
          name = "討伐"
          val requireList = questGateway.getQuestProgress(p).map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}個"
          }
          setButton(MenuButton(1, 6, Material.BOOK, s"$RESET[討伐クエスト]${selectedQuestData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストを完了した際に得られる経験値量:${selectedQuestData.exp}"
          )))
        }
      case None =>
    }
    setButton(MenuButton(9, 6, Material.RED_WOOL, s"$RED${BOLD}クエストを中止する",
      List(s"$RED${BOLD}クリックでクエストを中止します。",
        s"$RED$BOLD${UNDERLINE}納品したアイテムは戻りません！")
    ).setLeftClickMotion(new QuestProcessInventoryMotions(ryoServerAssist).questDestroy))
    buttons :+= getLayOut(1, 6)
    buttons :+= getLayOut(2, 6)
    buttons :+= getLayOut(9, 6)
    build(new QuestProcessMenu(ryoServerAssist).inventory)
    open()
  }

}

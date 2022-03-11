package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackService
import com.ryoserver.Quest.QuestServices.DailyQuestService
import com.ryoserver.Quest._
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DailyQuestProcessMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "デイリークエスト")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questService = new DailyQuestService(ryoServerAssist,player)
    val selectedQuestData = questService.getSelectedQuestData
    val compute = computeDailyQuestProcessButton(player, selectedQuestData, ryoServerAssist, this)
    import compute._
    val buttons = Map(
      getLayOut(1, 6) -> requireButton
    )
    if (compute.selectedQuest.questType == QuestType.delivery) {
      buttons ++ Map(
        getLayOut(2, 6) -> delivery,
        getLayOut(3, 6) -> deliveryFromNeoStack,
        getLayOut(9, 6) -> suspension
      )
    } else {
      buttons
    }
  }

}

private case class computeDailyQuestProcessButton(player: Player, selectedQuest: QuestDataContext, ryoServerAssist: RyoServerAssist, dailyQuestProcessMenu: DailyQuestProcessMenu) {
  private lazy val questService = new DailyQuestService(ryoServerAssist,player)
  private lazy val service = new NeoStackService
  private lazy val questType: String = if (selectedQuest.questType == QuestType.delivery) "納品" else "討伐"
  private lazy val requireDeliveryList: List[String] = new QuestPlayerData().getQuestData.getPlayerDailyQuestContext(player.getUniqueId).progress.get.map { case (require, amount) =>
    s"$WHITE${Translate.materialNameToJapanese(require.material)}:${amount}個"
  }.toList
  private lazy val requireSuppressionList: List[String] = new QuestPlayerData().getQuestData.getPlayerDailyQuestContext(player.getUniqueId).progress.get.map { case (require, amount) =>
    s"$WHITE${Translate.entityNameToJapanese(require.entityType)}:${amount}体"
  }.toList

  val requireButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"$RESET[${questType}クエスト]${selectedQuest.questName}")
      .lore(List(
        s"$WHITE【${questType}リスト】"
      ) ++ (if (selectedQuest.questType == QuestType.delivery) requireDeliveryList else requireSuppressionList) ++ List(
        "",
        s"${WHITE}このクエストを完了した際に得られる経験値量:${selectedQuest.exp}"
      ))
      .build()
  )

  val delivery: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHER_STAR)
      .title(s"${GREEN}納品する")
      .lore(List(s"${GRAY}クリックで納品します。"))
      .build(),
    ButtonMotion { _ =>
      questService.delivery()
    }
  )

  val deliveryFromNeoStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.SHULKER_BOX)
      .title(s"${GREEN}ネオスタックから納品")
      .lore(List(s"${GRAY}クリックでneoStackから納品します。") ++ new QuestPlayerData().getQuestData.getPlayerDailyQuestContext(player.getUniqueId).progress
        .get.map { case (require, amount) =>
        s"$WHITE${Translate.materialNameToJapanese(require.material)}:${
          val neoStackAmount = service.getItemAmount(player.getUniqueId, new ItemStack(require.material)).getOrElse(0)
          if (neoStackAmount >= amount) s"$AQUA$BOLD${UNDERLINE}OK (所持数:${neoStackAmount}個)"
          else s"$RED$BOLD$UNDERLINE${-(neoStackAmount - amount)}個不足しています"
        }"
      })
      .build(),
    ButtonMotion { _ =>
      questService.deliveryFromNeoStack()
    }
  )

  val suspension: Button = Button(
    ItemStackBuilder
      .getDefault(Material.RED_WOOL)
      .title(s"$RED${BOLD}クエストを中止する")
      .lore(List(
        s"$RED${BOLD}クリックでクエストを中止します。",
        s"$RED$BOLD${UNDERLINE}納品したアイテムは戻りません！")
      )
      .build(),
    ButtonMotion { _ =>
      questService.questDestroy()
      new SelectDailyQuestMenu(ryoServerAssist,1).open(player)
      player.playSound(player.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
    }
  )
}

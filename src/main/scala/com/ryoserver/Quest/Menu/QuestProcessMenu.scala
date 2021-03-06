package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackService
import com.ryoserver.Quest.QuestServices.NormalQuestService
import com.ryoserver.Quest.{QuestData, QuestDataContext, QuestPlayerData, QuestType}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class QuestProcessMenu(ryoServerAssist: RyoServerAssist,continueNumber: Int) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "クエスト")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questService = new NormalQuestService(player)
    questService.getSelectedQuest match {
      case Some(selectedQuest) =>
        val compute = computeQuestProcessButton(player, QuestData.loadedQuestData.filter(_.questName == selectedQuest).head,continueNumber,ryoServerAssist, this)
        import compute._
        val buttons = Map(
          getLayOut(1, 6) -> requireButton,
          getLayOut(9, 6) -> suspension
        )
        if (compute.selectedQuest.questType == QuestType.delivery) {
          buttons ++ Map(
            getLayOut(2, 6) -> delivery,
            getLayOut(3, 6) -> deliveryFromNeoStack,
            getLayOut(4, 6) -> continueDeliverySetup
          )
        } else {
          buttons
        }
      case None =>
        Map.empty
    }

  }

}

private case class computeQuestProcessButton(player: Player, selectedQuest: QuestDataContext,continueNumber: Int, ryoServerAssist: RyoServerAssist, questProcessMenu: QuestProcessMenu) {
  private lazy val service = new NeoStackService
  private lazy val questType: String = if (selectedQuest.questType == QuestType.delivery) "納品" else "討伐"
  private lazy val requireDeliveryList: List[String] = new QuestPlayerData().getQuestData.getPlayerQuestContext(player.getUniqueId).progress.get.map { case (require, amount) =>
    s"$WHITE${Translate.materialNameToJapanese(require.material)}:${amount * continueNumber}個"
  }.toList
  private lazy val requireSuppressionList: List[String] = new QuestPlayerData().getQuestData.getPlayerQuestContext(player.getUniqueId).progress.get.map { case (require, amount) =>
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
      val service = new NormalQuestService(player)
      service.delivery()
      new SelectQuestMenu(ryoServerAssist,1,new QuestPlayerData().getQuestData.getQuestSortData(player.getUniqueId)).open(player)
    }
  )

  lazy val deliveryFromNeoStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.SHULKER_BOX)
      .title(s"${GREEN}ネオスタックから納品")
      .lore(List(s"${GRAY}クリックでneoStackから納品します。") ++ new QuestPlayerData().getQuestData.getPlayerQuestContext(player.getUniqueId).progress
        .get
        .map { case (require, amount) =>
        s"$WHITE${Translate.materialNameToJapanese(require.material)}:${
          val neoStackAmount = service.getItemAmount(player.getUniqueId, new ItemStack(require.material)).getOrElse(0)
          if (neoStackAmount >= amount * continueNumber) s"$AQUA$BOLD${UNDERLINE}OK (所持数:${neoStackAmount}個)"
          else s"$RED$BOLD$UNDERLINE${-(neoStackAmount - amount * continueNumber)}個不足しています"
        }"
      })
      .build(),
    ButtonMotion { _ =>
      val service = new NormalQuestService(player)
      val selectedQuestName = service.getSelectedQuest.get
      for (_ <- 0 until continueNumber) {
        if (continueNumber != 1) service.selectQuest(selectedQuestName)
        service.deliveryFromNeoStack()
      }
      player.sendMessage(s"$AQUA${continueNumber}回納品を行いました。")
      new SelectQuestMenu(ryoServerAssist,1,new QuestPlayerData().getQuestData.getQuestSortData(player.getUniqueId)).open(player)
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
      new NormalQuestService(player).questDestroy()
      new SelectQuestMenu(ryoServerAssist,1,new QuestPlayerData().getQuestData.getQuestSortData(player.getUniqueId)).open(player)
      player.playSound(player.getLocation, Sound.BLOCK_ANVIL_DESTROY, 1, 1)
    }
  )

  val continueDeliverySetup: Button = Button(
    ItemStackBuilder
      .getDefault(Material.REPEATER)
      .title(s"${GREEN}ネオスタックから納品の連続設定")
      .lore(List(
        s"${WHITE}現在の設定: ${continueNumber}回",
        s"${GRAY}左クリックで増加、右クリックで減少します。",
        s"${GRAY}設定の変更はクエストを一度も",
        s"${GRAY}進行していない状態である必要があります。"
      ))
      .build(),
    ButtonMotion{e =>
      e.getClick match {
        case ClickType.LEFT =>
          val resetContinueNumber = {
            //設定の変更はクエストが一度も進行していない必要がある。
            //クエストが進行されていると、納品数が合わなくなるため。
            if (QuestData.loadedQuestData.exists(_.requireList == new QuestPlayerData().getQuestData.getPlayerQuestContext(player.getUniqueId).progress.get)) {
              continueNumber match {
                case 1 => 16
                case 16 => 32
                case 32 => 64
                case 64 => 128
                case 128 => 256
                case 256 => 512
                case 512 => 1024
                case 1024 => 1024
                case _ => 1
              }
            } else {
              1
            }
          }
          new QuestProcessMenu(ryoServerAssist,resetContinueNumber).open(player)
        case ClickType.RIGHT =>
          val resetContinueNumber = {
            if (QuestData.loadedQuestData.exists(_.requireList == new QuestPlayerData().getQuestData.getPlayerQuestContext(player.getUniqueId).progress.get)) {
              continueNumber match {
                case 1024 => 512
                case 512 => 256
                case 256 => 128
                case 128 => 64
                case 64 => 32
                case 32 => 16
                case 16 => 1
                case 1 => 1
                case _ => 1
              }
            } else {
              1
            }
          }
          new QuestProcessMenu(ryoServerAssist,resetContinueNumber).open(player)
        case _ =>

      }
      player.playSound(player.getLocation,Sound.UI_BUTTON_CLICK,1,1)
    }
  )


}

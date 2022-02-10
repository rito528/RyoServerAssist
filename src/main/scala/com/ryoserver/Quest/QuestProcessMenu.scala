package com.ryoserver.Quest

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.{ItemStackBuilder, Translate}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class QuestProcessMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6,"クエスト")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val questGateway = new QuestGateway
    questGateway.getSelectedQuest(player) match {
      case Some(selectedQuest) =>
        val compute = computeQuestProcessButton(player,selectedQuest,ryoServerAssist,this)
        import compute._
        val buttons = Map(
          getLayOut(1, 6) -> requireButton
        )
        if (compute.selectedQuest.questType == "delivery") {
          buttons ++ Map(
            getLayOut(2, 6) -> delivery,
            getLayOut(3, 6) -> deliveryFromNeoStack,
            getLayOut(9, 6) -> suspension
          )
        } else {
          buttons
        }
      case None =>
        Map.empty
    }

  }

}

private case class computeQuestProcessButton(player: Player,selectedQuest: QuestType,ryoServerAssist: RyoServerAssist,questProcessMenu: QuestProcessMenu) {
  lazy val questGateway = new QuestGateway
  lazy val requireDeliveryList: List[String] = questGateway.getQuestProgress(player).map { case (require, amount) =>
    s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
  }.toList
  lazy val requireSuppressionList: List[String] = questGateway.getQuestProgress(player).map { case (require, amount) =>
    s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}体"
  }.toList
  lazy val neoStackGateway = new NeoStackGateway
  lazy val questType: String = if (selectedQuest.questType == "delivery") "納品" else "討伐"

  val requireButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"$RESET[${questType}クエスト]${selectedQuest.questName}")
      .lore(List(
        s"$WHITE【${questType}リスト】"
      ) ++ (if (selectedQuest.questType == "delivery") requireDeliveryList else requireSuppressionList) ++ List(
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
    ButtonMotion{_ =>
      new QuestProcessInventoryMotions(ryoServerAssist).delivery(player)
    }
  )

  val deliveryFromNeoStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.SHULKER_BOX)
      .title(s"${GREEN}ネオスタックから納品")
      .lore(List(s"${GRAY}クリックでneoStackから納品します。") ++ questGateway.getQuestProgress(player).map { case (require, amount) =>
        s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${
          val neoStackAmount = neoStackGateway.getNeoStackAmount(player, new ItemStack(Material.matchMaterial(require)))
          if (neoStackAmount >= amount) s"$AQUA$BOLD${UNDERLINE}OK (所持数:${neoStackAmount}個)"
          else s"$RED$BOLD$UNDERLINE${-(neoStackAmount - amount)}個不足しています"
        }"
      })
      .build(),
    ButtonMotion{_ =>
      new QuestProcessInventoryMotions(ryoServerAssist).deliveryFromNeoStack(player)
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
    ButtonMotion{_ =>
      new QuestProcessInventoryMotions(ryoServerAssist).questDestroy(player)
    }
  )


}

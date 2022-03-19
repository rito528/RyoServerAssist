package com.ryoserver.Quest.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.QuestServices.DailyQuestService
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class DailyQuestRewardMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(1, "報酬選択画面")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeRewardButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(2, 1) -> getGachaTicket,
      getLayOut(4, 1) -> expBonus
    )
  }

}

private case class computeRewardButton(player: Player, ryoServerAssist: RyoServerAssist) {
  val getGachaTicket: Button = Button(
    ItemStackBuilder
      .getDefault(Material.PAPER)
      .setEffect()
      .title(s"${GREEN}ガチャ券を受け取る")
      .lore(List(s"${GRAY}報酬としてガチャ券を16枚受け取ります。"))
      .build(),
    ButtonMotion { _ =>
      questService.questClear(questService.getSelectedQuestData.exp)
      player.getRyoServerData.addGachaTickets(16).apply(player)
      player.sendMessage(s"${AQUA}デイリークエストの報酬として、ガチャ券を16枚配布しました。")
      player.playSound(player.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
      player.closeInventory()
    }
  )
  val expBonus: Button = Button(
    ItemStackBuilder
      .getDefault(Material.EXPERIENCE_BOTTLE)
      .title(s"${GREEN}今回のクエストの経験値1.2倍")
      .lore(List(s"${GRAY}報酬として今回のクエストの経験値を1.2倍にします。"))
      .build(),
    ButtonMotion { _ =>
      questService.questClear(questService.getSelectedQuestData.exp * 1.2)
      player.sendMessage(s"${AQUA}デイリークエストの報酬として、今回のクエストの経験値を1.2倍にしました。")
      player.playSound(player.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
      player.closeInventory()
    }
  )
  private val questService = new DailyQuestService(ryoServerAssist,player)

}

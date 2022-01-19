package com.ryoserver.Quest

import com.ryoserver.Menu.{Menu, MenuButton}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Player.PlayerManager.setPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class DailyQuestRewardMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 1
  override var name: String = "報酬選択画面"
  override var p: Player = _

  def openRewardMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(2,1,Material.PAPER,s"${GREEN}ガチャ券を受け取る",List(s"${GRAY}報酬としてガチャ券を16枚受け取ります。"))
    .setLeftClickMotion(rewardGachaTicket)
    .setEffect())
    setButton(MenuButton(4,1,Material.EXPERIENCE_BOTTLE,s"${GREEN}今回のクエストの経験値1.2倍",List(s"${GRAY}報酬として今回のクエストの経験値を1.2倍にします。"))
    .setLeftClickMotion(rewardQuestExp)
    .setEffect())
    build(new DailyQuestRewardMenu(ryoServerAssist).openRewardMenu)
    open()
  }

  private def rewardGachaTicket(p: Player): Unit = {
    val questGateway = new QuestGateway
    questGateway.dailyQuestClear(p, ryoServerAssist,1.0)
    p.giveNormalGachaTickets(16)
    p.sendMessage(s"${AQUA}デイリークエストの報酬として、ガチャ券を16枚配布しました。")
    p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    p.closeInventory()
  }

  private def rewardQuestExp(p: Player): Unit = {
    val questGateway = new QuestGateway
    questGateway.dailyQuestClear(p, ryoServerAssist,1.2)
    p.sendMessage(s"${AQUA}デイリークエストの報酬として、今回のクエストの経験値を1.2倍にしました。")
    p.playSound(p.getLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
    p.closeInventory()
  }

}

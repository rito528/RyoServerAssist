package com.ryoserver.Quest

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Player.PlayerManager.setPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class DailyQuestRewardMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 1
  override var name: String = "報酬選択画面"
  override var p: Player = _

  def openRewardMenu(player: Player): Unit = {
    p = player
    setItem(2,1,Material.PAPER,effect = true,s"${GREEN}ガチャ券を受け取る",List(s"${GRAY}報酬としてガチャ券を16枚受け取ります。"))
    setItem(4,1,Material.EXPERIENCE_BOTTLE,effect = true,s"${GREEN}今回のクエストの経験値1.2倍",List(s"${GRAY}報酬として今回のクエストの経験値を1.2倍にします。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index: Int): Unit = {
    val questGateway = new QuestGateway
    if (index == getLayOut(2,1)) {
      questGateway.dailyQuestClear(p, ryoServerAssist,1.0)
      p.giveNormalGachaTickets(16)
      p.sendMessage(s"${AQUA}デイリークエストの報酬として、ガチャ券を16枚配布しました。")
    } else if (index == getLayOut(4,1)) {
      questGateway.dailyQuestClear(p, ryoServerAssist,1.2)
      p.sendMessage(s"${AQUA}デイリークエストの報酬として、今回のクエストの経験値を1.2倍にしました。")
    }
    p.closeInventory()
  }

}

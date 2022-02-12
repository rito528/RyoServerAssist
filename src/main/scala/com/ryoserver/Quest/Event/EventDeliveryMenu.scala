package com.ryoserver.Quest.Event

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Quest.Event.EventDataProvider.{eventCounter, eventRanking}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EventDeliveryMenu(implicit ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "イベント・納品")
  override val partButton: Boolean = true

  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    val gateway = new EventGateway()
    if (gateway.holdingEvent() == null) {
      player.sendMessage(s"${RED}イベントが終了しました！")
      return false
    }
    true
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeEventDeliveryMenuButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(2, 6) -> deliveryButton
    )
  }

}

private case class computeEventDeliveryMenuButton(player: Player, ryoServerAssist: RyoServerAssist) {
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}イベントページに戻る")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      new EventMenu(ryoServerAssist).open(player)
    }
  )

  val deliveryButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHER_STAR)
      .title(s"${GREEN}納品する")
      .lore(List(s"${GRAY}クリックで納品します。"))
      .build(),
    ButtonMotion { _ =>
      val gateway = new EventGateway()
      if (gateway.holdingEvent() == null) {
        player.sendMessage(s"${RED}イベントが終了したため、納品できませんでした。")
      } else {
        val nowEvent = gateway.eventInfo(gateway.holdingEvent())
        val material = Material.matchMaterial(nowEvent.item)
        val inv = player.getOpenInventory.getTopInventory
        var exp = 0.0
        var amount = 0
        inv.getContents.zipWithIndex.foreach { case (itemStack, index) =>
          if (itemStack != null && itemStack.getType == material) {
            amount += itemStack.getAmount
            inv.clear(index)
            exp += itemStack.getAmount * gateway.eventInfo(gateway.holdingEvent()).exp
          }
        }
        eventCounter += amount
        new EventDeliveryMenu().open(player)
        new UpdateLevel().addExp(exp, player)
        if (!eventRanking.contains(player.getUniqueId.toString)) {
          eventRanking += (player.getUniqueId.toString -> amount)
        } else {
          val oldAmount = eventRanking(player.getUniqueId.toString)
          eventRanking = eventRanking
            .filterNot { case (uuid, _) => uuid == player.getUniqueId.toString }
          eventRanking += (player.getUniqueId.toString -> (oldAmount + amount))
        }
        player.sendMessage(s"${AQUA}納品しました。")
        player.sendMessage(s"$AQUA${exp.toString}exp手に入りました。(ボーナス分を除く)")
      }
    }
  )
}

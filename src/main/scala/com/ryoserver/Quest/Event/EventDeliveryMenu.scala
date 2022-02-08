package com.ryoserver.Quest.Event

import com.ryoserver.Level.Player.UpdateLevel
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{MenuOld, MenuButton, MenuSessions}
import com.ryoserver.Quest.Event.EventDataProvider.{eventCounter, eventRanking}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.{ChatColor, Material}

class EventDeliveryMenu(implicit ryoServerAssist: RyoServerAssist) extends MenuOld with Listener {

  override val slot: Int = 6
  override var name: String = "イベント・納品"
  override var p: Player = _

  def openMenu(player: Player): Unit = {
    p = player
    val gateway = new EventGateway()
    if (gateway.holdingEvent() == null) {
      p.sendMessage(s"${RED}イベントが終了しました！")
    } else {
      setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${ChatColor.AQUA}イベントページに戻る", List(s"${ChatColor.GRAY}クリックで戻ります。"))
        .setLeftClickMotion(backPage))
      setButton(MenuButton(2, 6, Material.NETHER_STAR, s"${ChatColor.YELLOW}納品", List(s"${ChatColor.GRAY}クリックで納品します。"))
        .setLeftClickMotion(delivery)
        .setReload())
      partButton = true
      buttons :+= getLayOut(1, 6)
      buttons :+= getLayOut(2, 6)
      build(new EventDeliveryMenu().openMenu)
      open()
    }
  }

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    val inv = e.getView.getTopInventory
    if (inv.getHolder != MenuSessions.session || e.getView.getTitle != name) return
    inv.getContents.toList.zipWithIndex.foreach { case (is, index) =>
      if (index != getLayOut(1, 6) && index != getLayOut(2, 6) && is != null) e.getPlayer.getLocation.getWorld.dropItem(e.getPlayer.getLocation, is)
    }
  }

  private def backPage(p: Player): Unit = {
    new EventMenu(ryoServerAssist).openEventMenu(p)
  }

  private def delivery(p: Player): Unit = {
    val gateway = new EventGateway()
    if (gateway.holdingEvent() == null) {
      p.sendMessage(s"${RED}イベントが終了したため、納品できませんでした。")
    } else {
      val nowEvent = gateway.eventInfo(gateway.holdingEvent())
      val material = Material.matchMaterial(nowEvent.item)
      val inv = p.getOpenInventory.getTopInventory
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
      new EventDeliveryMenu().openMenu(p)
      new UpdateLevel().addExp(exp, p)
      if (!eventRanking.contains(p.getUniqueId.toString)) {
        eventRanking += (p.getUniqueId.toString -> amount)
      } else {
        val oldAmount = eventRanking(p.getUniqueId.toString)
        eventRanking = eventRanking
          .filterNot { case (uuid, _) => uuid == p.getUniqueId.toString }
        eventRanking += (p.getUniqueId.toString -> (oldAmount + amount))
      }
      p.sendMessage(s"${AQUA}納品しました。")
      p.sendMessage(s"${AQUA}${exp.toString}exp手に入りました。(ボーナス分を除く)")
    }
  }

}

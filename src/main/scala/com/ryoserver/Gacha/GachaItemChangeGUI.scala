package com.ryoserver.Gacha

import com.ryoserver.Gacha.GachaItemChangeItems.items
import com.ryoserver.util.Item.getItem
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, CreateMenu}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.{Bukkit, ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryCloseEvent}
import org.bukkit.event.{EventHandler, Listener}

import scala.jdk.CollectionConverters._

class GachaItemChangeGUI(ryoServerAssist: RyoServerAssist) extends Listener with Menu {

  val RETE: Int = ryoServerAssist.getConfig.getInt("gachaChangeRete")
  override var name: String = "ガチャ特等取引"
  override val slot: Int = 6
  override var p: Player = _

  def openChangeGUI(player: Player): Unit = {
    p = player
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, "menuに戻る", List("クリックでmenuに戻ります。"))
    setItem(5, 6, Material.NETHER_STAR, effect = false, "クリックでインベントリ内の特等アイテムを交換します。", List(
      "クリックでガチャアイテムを交換します。", s"特等アイテム1個 -> スキル回復(大)${RETE}個"
    ))
    partButton = true
    buttons :+= getLayOut(1, 6)
    buttons :+= getLayOut(5, 6)
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    var changeAmount = 0
    if (index == 45) {
      new CreateMenu(ryoServerAssist).menu(p)
      return
    }
    inv.get.getContents.foreach(itemStack => {
      if (itemStack != null && items.contains(itemStack)) {
        changeAmount += RETE
      }
    })
    if (changeAmount != 0 && index == 49) {
      val item = RecoveryItems.max
      item.setAmount(changeAmount)
      p.getWorld.dropItem(p.getLocation, item)
      p.sendMessage(ChatColor.AQUA + "特等アイテムを" + changeAmount + "個のスキル回復(大)と交換しました。")
      inv.get.clear()
      new GachaItemChangeGUI(ryoServerAssist).openChangeGUI(p)
    }
  }

  @EventHandler
  def onClose(e: InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != name) return
    var index = 0
    val p = e.getPlayer
    var check = false
    e.getInventory.getContents.foreach(item => {
      if (item != null && index != 49 && index != 45) {
        p.getWorld.dropItem(p.getLocation, item)
        check = true
      }
      index += 1
    })
    if (check) p.sendMessage(ChatColor.RED + "不要なアイテムを返却しました。")
  }

}

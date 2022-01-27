package com.ryoserver.Gacha

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Gacha.GachaItemChangeItems.items
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class GachaItemChangeGUI(ryoServerAssist: RyoServerAssist) extends Listener with Menu {

  override val slot: Int = 6
  val RETE: Int = getConfig.gachaChangeRate
  override var name: String = "ガチャ特等取引"
  override var p: Player = _

  def openChangeGUI(player: Player): Unit = {
    p = player
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}menuに戻る", List(s"${GRAY}クリックでmenuに戻ります。"))
    .setLeftClickMotion(backMenu))
    setButton(MenuButton(5, 6, Material.NETHER_STAR, s"$RESET${GREEN}クリックでインベントリ内の特等アイテムを交換します。", List(
      s"${GRAY}クリックでガチャアイテムを交換します。", s"${GRAY}特等アイテム1個 -> スキル回復(大)${RETE}個"
    ))
    .setLeftClickMotion(changeItem)
    .setReload())
    partButton = true
    buttons :+= getLayOut(1, 6)
    buttons :+= getLayOut(5, 6)
    build(new GachaItemChangeGUI(ryoServerAssist).openChangeGUI)
    open()
  }

  private def backMenu(p: Player): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(p)
  }

  private def changeItem(p: Player): Unit = {
    var changeAmount = 0
    inv.get.getContents.foreach(itemStack => {
      if (itemStack != null && GachaLoader.specialItemList.contains(itemStack)) {
        changeAmount += RETE
      }
    })
    if (changeAmount != 0) {
      val item = RecoveryItems.max.clone()
      item.setAmount(changeAmount)
      p.getWorld.dropItem(p.getLocation, item)
      p.sendMessage(s"${AQUA}特等アイテムを${changeAmount}個のスキル回復(大)と交換しました。")
      inv.get.getContents.zipWithIndex.foreach { case (is, index) =>
        if (GachaLoader.specialItemList.contains(is)) inv.get.clear(index)
      }
      new GachaItemChangeGUI(ryoServerAssist).openChangeGUI(p)
    } else {
      p.sendMessage(s"${RED}特等アイテムが見つかりませんでした。")
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
    if (check) p.sendMessage(s"${RED}不要なアイテムを返却しました。")
  }

}

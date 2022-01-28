package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton}
import com.ryoserver.NeoStack.NeoStackGateway
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.{EventHandler, Listener}

class SelectStackMenu extends Menu with Listener {

  override val slot: Int = 6
  override var name: String = "ネオスタック選択収納"
  override var p: Player = _
  partButton = true

  def openSelectStackMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(5,6,Material.CHEST_MINECART,s"${GREEN}neoStackに収納します",List(s"${GRAY}クリックで収納します"))
    .setLeftClickMotion(stack)
    .setReload())
    buttons :+= getLayOut(5,6)
    build(new SelectStackMenu().openSelectStackMenu)
    open()
  }

  private def stack(p: Player): Unit = {
    val neoStackGateway = new NeoStackGateway
    p.getOpenInventory.getTopInventory.getContents.foreach(item => {
      if (item != null) {
        if (neoStackGateway.checkItemList(item)) {
          neoStackGateway.addStack(item, p)
          p.getInventory.removeItem(item)
        }
      }
    })
    p.sendMessage(s"${AQUA}選択されたアイテムをneoStackに収納しました。")
  }

  @EventHandler
  def closeEvent(e: InventoryCloseEvent): Unit = {
    if (e.getPlayer.getOpenInventory.getTitle != "ネオスタック選択収納") return
    val inv = e.getInventory
    inv.clear(getLayOut(5,6))
    var isDropped = false
    inv.getContents.foreach{content =>
      if (content != null) {
        p.getWorld.dropItem(p.getLocation,content)
        if (!isDropped) isDropped = true
      }
    }
    if (isDropped) p.sendMessage(s"${AQUA}収納できないアイテムをドロップしました。")
  }

}

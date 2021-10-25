package com.ryoserver.Gacha

import com.ryoserver.Gacha.GachaItemChangeItems.items
import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.Menu.createMenu
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.{Bukkit, ChatColor, Material}
import org.bukkit.entity.Player
import org.bukkit.event.inventory.{InventoryClickEvent, InventoryCloseEvent}
import org.bukkit.event.{EventHandler, Listener}

import scala.jdk.CollectionConverters._

class GachaItemChangeGUI(ryoServerAssist: RyoServerAssist) extends Listener {

  val RETE: Int = ryoServerAssist.getConfig.getInt("gachaChangeRete")
  val title = "ガチャ特等取引"

  def openChangeGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,title)
    inv.setItem(49,getItem(Material.NETHER_STAR,"クリックでインベントリ内の特等アイテムを交換します。",
      List("クリックでガチャアイテムを交換します。",s"特等アイテム1個 -> スキル回復(大)${RETE}個").asJava))
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,"menuに戻る",List("クリックでmemuに戻ります。").asJava))
    p.openInventory(inv)
  }

  @EventHandler
  def onClick(e:InventoryClickEvent): Unit = {
    if (e.getView.getTitle != title || e.getClickedInventory != e.getView.getTopInventory) return
    if (e.getSlot == 49 || e.getSlot == 45) e.setCancelled(true)
    var changeAmount = 0
    val p = e.getWhoClicked.asInstanceOf[Player]
    if (e.getSlot == 45) {
      p.openInventory(createMenu.menu(p, ryoServerAssist))
      return
    }
    e.getClickedInventory.getContents.foreach(itemStack => {
      if (itemStack != null && items.contains(itemStack)) {
        changeAmount += RETE
      }
    })
    if (changeAmount != 0 && e.getSlot == 49) {
      val item = RecoveryItems.max
      item.setAmount(changeAmount)
      p.getWorld.dropItem(p.getLocation, item)
      p.sendMessage(ChatColor.AQUA + "特等アイテムを" + changeAmount + "個のスキル回復(大)と交換しました。")
      e.getView.getTopInventory.clear()
      openChangeGUI(p)
    }
  }

  @EventHandler
  def onClose(e:InventoryCloseEvent): Unit = {
    if (e.getView.getTitle != title) return
    var index = 0
    val p = e.getPlayer
    var check = false
    e.getInventory.getContents.foreach(item => {
      if (item != null && index != 49 && index != 45) {
        p.getWorld.dropItem(p.getLocation,item)
        check = true
      }
      index += 1
    })
    if (check) p.sendMessage(ChatColor.RED + "不要なアイテムを返却しました。")
  }

}

package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{Menu, MenuData}
import com.ryoserver.NeoStack.ItemData.itemData
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.PlayerData.playerData
import com.ryoserver.NeoStack.{ItemData, NeoStackData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class StackMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override var name: String = _
  override val slot: Int = 6
  override var p: Player = _

  def openStack(player: Player, page: Int, category: String): Unit = {
    name = "neoStack:" + page
    p = player
    val uuid = p.getUniqueId.toString
    var index = 0
    var invItems = ""
    val data = new NeoStackData(ryoServerAssist).getItemAmount(category, p)
    if (stackPageData.contains(category) && stackPageData(category).contains(page)) invItems = stackPageData(category)(page)
    invItems.split(";").foreach(item => {
      val is = Item.getItemStackFromString(item)
      if (is != null) {
        is.setAmount(1)
        val setItem = is.clone()
        val meta = is.getItemMeta
        var amount = 0
        if (playerData.contains(uuid) && playerData(uuid).contains(is)) {
          amount = playerData(uuid)(is)
        } else {
          if (data.contains(is)) amount = data(is)
        }
        meta.setLore(List(
          s"$BLUE${BOLD}保有数:$UNDERLINE${amount}個",
          s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
        ).asJava)
        is.setItemMeta(meta)
        if (!ItemData.itemData.contains(p.getName)) ItemData.itemData += (p.getName -> mutable.Map(is -> setItem))
        else ItemData.itemData(p.getName) += (is -> setItem)
        if (!playerData.contains(p.getUniqueId.toString)) playerData += (p.getUniqueId.toString -> mutable.Map(setItem -> amount))
        else playerData(p.getUniqueId.toString) += (setItem -> amount)
        setItemStack(getX(index), getY(index), is)
      }
      index += 1
    })
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}前のページに戻ります。", List(s"${GRAY}クリックで戻ります。"))
    if (p.hasPermission("ryoserverassist.neoStack")) {
      setItem(5, 6, Material.CHEST, effect = false, s"${AQUA}アイテムを追加します。", List(s"${GRAY}クリックで追加メニューを開きます。"))
    }
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerNeedClickMotion(motion)
    open()
  }

  def registerNeedClickMotion(func: (Player, Int, Boolean) => Unit): Unit = {
    MenuData.dataNeedClick += (name -> func)
    MenuData.partButton += (name -> partButton)
    MenuData.Buttons += (name -> buttons)
  }

  def motion(p: Player, index: Int, isRightClick: Boolean): Unit = {
    val nowPage = p.getOpenInventory.getTitle.replace("neoStack:", "").replace("[Edit]", "").toInt
    val categoryMenu = new CategorySelectMenu(ryoServerAssist)
    index match {
      case 45 =>
        val backPage = nowPage - 1
        if (backPage == 0) categoryMenu.openCategorySelectMenu(p)
        else new StackMenu(ryoServerAssist).openStack(p, backPage, getSelectedCategory(p))
      case 49 =>
        if (p.hasPermission("ryoserverassist.neoStack")) new NeoStackEditGUI(ryoServerAssist).openAddGUI(p, 1, getSelectedCategory(p))
      case 53 =>
        new StackMenu(ryoServerAssist).openStack(p, nowPage + 1, getSelectedCategory(p))
      case _ =>
        val is = inv.get.getItem(index)
        val data = new NeoStackData(ryoServerAssist)
        if (!itemData.contains(p.getName) || !itemData(p.getName).contains(is)) return
        if (isRightClick) {
          data.addItemToPlayer(p, ItemData.itemData(p.getName)(is), 1)
        } else if (!isRightClick) {
          data.addItemToPlayer(p, ItemData.itemData(p.getName)(is), is.getType.getMaxStackSize)
        }
        new StackMenu(ryoServerAssist).openStack(p, nowPage, getSelectedCategory(p))
    }
  }

}
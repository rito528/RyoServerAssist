package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{Menu, MenuData}
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.{NeoStackDataType, NeoStackGateway, PlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class StackMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = _
  override var p: Player = _

  def openStack(player: Player, page: Int, category: String): Unit = {
    name = "neoStack:" + page
    p = player
    var index = 0
    var invItems = ""
    if (stackPageData.contains(category) && stackPageData(category).contains(page)) invItems = stackPageData(category)(page)
    invItems.split(";").foreach(item => {
      if (Item.getItemStackFromString(item) != null) {
        val is = Item.getOneItemStack(Item.getItemStackFromString(item))
        val data = PlayerData.playerData.filter(data => data.uuid == p.getUniqueId.toString && data.savingItemStack == is)
        PlayerData.playerData = PlayerData.playerData
          .filterNot {
            data => data.uuid == p.getUniqueId.toString && data.savingItemStack == is
          }
        PlayerData.playerData :+= NeoStackDataType(p.getUniqueId.toString, is, null, if (data.nonEmpty) data.head.amount else 0)
        val playerData = PlayerData.playerData
          .filter(_.uuid == p.getUniqueId.toString)
          .filter(_.savingItemStack == is)
        if (is != null) {
          val setItem = is.clone()
          val meta = setItem.getItemMeta
          meta.setLore(List(
            s"$BLUE${BOLD}保有数:${UNDERLINE}${if (playerData.isEmpty) 0 else playerData.head.amount}個",
            s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
          ).asJava)
          setItem.setItemMeta(meta)
          if (playerData.nonEmpty) {
            PlayerData.playerData = PlayerData.playerData.filterNot(data => data.uuid == p.getUniqueId.toString && data.savingItemStack == is)
            PlayerData.playerData :+= NeoStackDataType(p.getUniqueId.toString, is, setItem, playerData.head.amount)
          }
          setItemStack(getX(index), getY(index), setItem)
        }
        index += 1
      }
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
        val data = new NeoStackGateway(ryoServerAssist)
        val playerData = PlayerData.playerData.filter(data => data.uuid == p.getUniqueId.toString && data.displayItemStack == is)
        if (playerData.isEmpty) return
        if (isRightClick) {
          data.addItemToPlayer(p, playerData.head.savingItemStack, 1)
        } else if (!isRightClick) {
          data.addItemToPlayer(p, playerData.head.savingItemStack, is.getType.getMaxStackSize)
        }
        new StackMenu(ryoServerAssist).openStack(p, nowPage, getSelectedCategory(p))
    }
  }

}
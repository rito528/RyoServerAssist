package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{MenuOld, MenuButton, MenuItemStack}
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.{NeoStackDataType, NeoStackGateway, PlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Item
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class StackMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  override val slot: Int = 6
  override var name: String = _
  override var p: Player = _

  def openStack(player: Player, page: Int, category: String): Unit = {
    name = "neoStack:" + page
    p = player
    var index = 0
    var invItems = ""
    if (stackPageData.contains(category) && stackPageData(category).contains(page)) invItems = stackPageData(category)(page)
    invItems.split(";").zipWithIndex.foreach { case (item, slot) =>
      if (Item.getItemStackFromString(item) != null) {
        val is = Item.getOneItemStack(Item.getItemStackFromString(item))
        val data = PlayerData.playerData.filter(data => data.uuid == p.getUniqueId && data.savingItemStack == is)
        PlayerData.playerData = PlayerData.playerData
          .filterNot {
            data => data.uuid == p.getUniqueId && data.savingItemStack == is
          }
        PlayerData.playerData += NeoStackDataType(p.getUniqueId, is, null, if (data.nonEmpty) data.head.amount else 0)
        val playerData = PlayerData.playerData
          .filter(_.uuid == p.getUniqueId)
          .filter(_.savingItemStack == is)
        if (is != null) {
          val setItem = is.clone()
          val meta = setItem.getItemMeta
          meta.setLore(List(
            s"$BLUE${BOLD}保有数:$UNDERLINE${if (playerData.isEmpty) 0 else playerData.head.amount}個",
            s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
          ).asJava)
          setItem.setItemMeta(meta)
          if (playerData.nonEmpty) {
            PlayerData.playerData = PlayerData.playerData.filterNot(data => data.uuid == p.getUniqueId && data.savingItemStack == is)
            PlayerData.playerData += NeoStackDataType(p.getUniqueId, is, setItem, playerData.head.amount)
          }
          setItemStackButton(MenuItemStack(getX(index), getY(index), setItem)
            .setLeftClickMotion(stackItem(_, slot))
            .setRightClickMotion(oneItem(_, slot))
            .setReload())
        }
        index += 1
      }
    }
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backPage))
    if (p.hasPermission("ryoserverassist.neoStack")) {
      setButton(MenuButton(5, 6, Material.CHEST, s"${AQUA}アイテムを追加します。", List(s"${GRAY}クリックで追加メニューを開きます。"))
        .setLeftClickMotion(openAddGUI))
    }
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(nextPage))
    build(new StackMenu(ryoServerAssist).openStack(_, page, category))
    open()
  }

  private def backPage(p: Player): Unit = {
    val nowPage = p.getOpenInventory.getTitle
      .replace("neoStack:", "")
      .replace("[Edit]", "").toInt
    val categoryMenu = new CategorySelectMenu(ryoServerAssist)
    val backPage = nowPage - 1
    if (backPage == 0) categoryMenu.open(p)
    else new StackMenu(ryoServerAssist).openStack(p, backPage, getSelectedCategory(p))
  }

  private def nextPage(p: Player): Unit = {
    val nowPage = p.getOpenInventory.getTitle
      .replace("neoStack:", "")
      .replace("[Edit]", "").toInt
    new StackMenu(ryoServerAssist).openStack(p, nowPage + 1, getSelectedCategory(p))
  }

  private def openAddGUI(p: Player): Unit = {
    new NeoStackEditGUI(ryoServerAssist).openAddGUI(p, 1, getSelectedCategory(p))
  }

  private def oneItem(p: Player, index: Int): Unit = {
    val is = inv.get.getItem(index)
    val data = new NeoStackGateway()
    val playerData = PlayerData.playerData.filter(data => data.uuid == p.getUniqueId && data.displayItemStack == is)
    if (playerData.isEmpty) return
    data.addItemToPlayer(p, playerData.head.savingItemStack, 1)
  }

  private def stackItem(p: Player, index: Int): Unit = {
    val is = inv.get.getItem(index)
    val data = new NeoStackGateway()
    val playerData = PlayerData.playerData.filter(data => data.uuid == p.getUniqueId && data.displayItemStack == is)
    if (playerData.isEmpty) return
    data.addItemToPlayer(p, playerData.head.savingItemStack, is.getType.getMaxStackSize)
  }

}
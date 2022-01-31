package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton, MenuItemStack, RyoServerMenu1}
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.{ItemList, LoadNeoStackPage, NeoStackGateway}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import scala.collection.mutable

class NeoStackEditGUI(ryoServerAssist: RyoServerAssist) extends Menu {
  override val slot: Int = 6
  override var name: String = _
  override var p: Player = _

  def openAddGUI(player: Player, page: Int, category: String): Unit = {
    p = player
    name = "neoStackアイテム追加メニュー:" + page
    val sql = new SQL()
    val rs = sql.executeQuery(s"SELECT * FROM StackList WHERE page=$page AND category='$category';")
    var invContents = ""
    if (rs.next()) invContents = rs.getString("invItem")
    var index = 0
    invContents.split(';').foreach(invContent => {
      val config = new YamlConfiguration
      config.loadFromString(invContent)
      if (invContent != null) {
        setItemStackButton(MenuItemStack(getX(index), getY(index), config.getItemStack("i", null)))
      }
      index += 1
    })
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backMenu))
    setButton(MenuButton(5, 6, Material.NETHER_STAR, "クリックでリストを保存します。", List("カテゴリ:" + getSelectedCategory(p) + "のリストを保存します。"))
      .setLeftClickMotion(save))
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
      .setLeftClickMotion(nextPage))
    partButton = true
    buttons :+= 45
    buttons :+= 49
    buttons :+= 53
    sql.close()
    build(new NeoStackEditGUI(ryoServerAssist).openAddGUI(_, 1, null))
    open()
  }

  private def backMenu(p: Player): Unit = {
    val nowPage = p.getOpenInventory.getTitle.replace("neoStackアイテム追加メニュー:", "").toInt
    if (nowPage != 1) {
      new NeoStackEditGUI(ryoServerAssist).openAddGUI(p, nowPage - 1, getSelectedCategory(p))
    } else if (nowPage == 1) {
      new RyoServerMenu1(ryoServerAssist).menu(p)
    }
  }

  private def save(p: Player): Unit = {
    val nowPage = p.getOpenInventory.getTitle.replace("neoStackアイテム追加メニュー:", "").toInt
    val data = new NeoStackGateway()
    var invIndex = 0
    var invItem = ""
    p.getOpenInventory.getTopInventory.getContents.foreach(is => {
      if (invIndex < 45) {
        invItem += Item.getStringFromItemStack(is) + ";"
      }
      invIndex += 1
    })
    data.editItemList(getSelectedCategory(p), nowPage, invItem)
    p.sendMessage(s"${AQUA}カテゴリリスト:${getSelectedCategory(p)}を編集しました。")
    new LoadNeoStackPage().loadStackPage()
    ItemList.stackList = mutable.Map.empty
    ItemList.loadItemList()
  }

  private def nextPage(p: Player): Unit = {
    val nowPage = p.getOpenInventory.getTitle.replace("neoStackアイテム追加メニュー:", "").toInt
    new NeoStackEditGUI(ryoServerAssist).openAddGUI(p, nowPage + 1, getSelectedCategory(p))
  }

}

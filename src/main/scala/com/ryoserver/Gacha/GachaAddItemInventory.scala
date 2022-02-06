package com.ryoserver.Gacha

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class GachaAddItemInventory(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = "ガチャアイテム追加メニュー"
  override var p: Player = _
  partButton = true

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  def openAddInventory(player: Player): Unit = {
    p = player
    setButton(MenuButton(2, 6, Material.EXPERIENCE_BOTTLE, "はずれにアイテムを追加します。", List("クリックでインベントリに入っているアイテムを",
      "はずれアイテムに追加します。"))
      .setLeftClickMotion(addItem(_, 0))
      .setReload())
    setButton(MenuButton(4, 6, Material.PHANTOM_MEMBRANE, "あたりにアイテムを追加します。", List("クリックでインベントリに入っているアイテムを",
      "あたりアイテムに追加します。"))
      .setLeftClickMotion(addItem(_, 1))
      .setReload())
    setButton(MenuButton(6, 6, Material.DIAMOND, "大当たりにアイテムを追加します。", List("クリックでインベントリに入っているアイテムを",
      "大当たりアイテムに追加します。"))
      .setLeftClickMotion(addItem(_, 2))
      .setReload())
    setButton(MenuButton(8, 6, Material.NETHERITE_INGOT, "特等にアイテムを追加します。", List("クリックでインベントリに入っているアイテムを",
      "特等アイテムに追加します。"))
      .setLeftClickMotion(addItem(_, 3))
      .setReload())
    buttons :+= getLayOut(2,6)
    buttons :+= getLayOut(4,6)
    buttons :+= getLayOut(6,6)
    buttons :+= getLayOut(8,6)
    build(new GachaAddItemInventory(ryoServerAssist).openAddInventory)
    open()
  }

  private def addItem(p: Player, rarity: Int): Unit = {
    add(inv.get, rarity)
    p.sendMessage(s"${AQUA}はずれガチャアイテムを追加しました。")
  }

  private def add(inv: Inventory, rarity: Int): Unit = {
    List(46,48,50,52).foreach(inv.clear)
    inv.getContents.foreach(is => {
      if (is != null) GachaLoader.addGachaItem(ryoServerAssist, is, rarity)
    })
  }

}

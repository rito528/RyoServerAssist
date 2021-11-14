package com.ryoserver.NeoStack.Menu

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.NeoStack.NeoStackData
import com.ryoserver.NeoStack.PlayerCategory.setSelectedCategory
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Material, Sound}

class CategorySelectMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override var name: String = "neoStackカテゴリ選択"
  override val slot: Int = 5
  override var p: Player = _

  def openCategorySelectMenu(player: Player): Unit = {
    p = player
    val data = new GetPlayerData(ryoServerAssist)
    if (data.getPlayerLevel(p) >= 20) {
      val lore: List[String] = List(s"${GRAY}クリックで開きます。")
      setItem(3, 2, Material.GRASS_BLOCK, effect = false, s"${GREEN}主要ブロック", lore)
      setItem(5, 2, Material.LEATHER, effect = false, s"${GREEN}主要アイテム", lore)
      setItem(7, 2, Material.HONEY_BOTTLE, effect = false, s"${GREEN}ガチャアイテム", lore)
      setItem(2, 3, Material.DIAMOND_PICKAXE, effect = false, s"${GREEN}ツール系", lore)
      setItem(4, 3, Material.BREAD, effect = false, s"${GREEN}食料系", lore)
      setItem(6, 3, Material.REDSTONE, effect = false, s"${GREEN}レッドストーン系", lore)
      setItem(8, 3, Material.OAK_SAPLING, effect = false, s"${GREEN}植物系", lore)
      setItem(1, 5, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
      setItem(5, 5, Material.HOPPER, effect = false, s"${WHITE}自動収納を${if (new NeoStackData(ryoServerAssist).isAutoStackEnabled(p)) "off" else "on"}にします。",
        List(s"${GRAY}クリックで切り替えます。",
          s"${GRAY}現在の状態:${if (new NeoStackData(ryoServerAssist).isAutoStackEnabled(p)) s"${GREEN}${BOLD}${UNDERLINE}on" else s"${RED}${BOLD}${UNDERLINE}off"}"))
      setItem(9, 5, Material.CHEST_MINECART, effect = false, s"${GREEN}インベントリ内のアイテムをstackに収納します。", List(s"${GRAY}クリックで収納します。"))
      registerMotion(motion)
      open()
      p.playSound(p.getLocation, Sound.BLOCK_SHULKER_BOX_OPEN, 1, 1)
    } else {
      p.sendMessage(ChatColor.RED + "neoStackはLv.20以上になると使うことができます。")
    }
  }

  def motion(p: Player, index: Int): Unit = {
    val data = new NeoStackData(ryoServerAssist)
    val gui = new StackMenu(ryoServerAssist)
    index match {
      case 11 =>
        gui.openStack(p, 1, "block")
        setSelectedCategory(p, "block")
      case 13 =>
        gui.openStack(p, 1, "item")
        setSelectedCategory(p, "item")
      case 15 =>
        gui.openStack(p, 1, "gachaItem")
        setSelectedCategory(p, "gachaItem")
      case 19 =>
        gui.openStack(p, 1, "tool")
        setSelectedCategory(p, "tool")
      case 21 =>
        gui.openStack(p, 1, "food")
        setSelectedCategory(p, "food")
      case 23 =>
        gui.openStack(p, 1, "redstone")
        setSelectedCategory(p, "redstone")
      case 25 =>
        gui.openStack(p, 1, "plant")
        setSelectedCategory(p, "plant")
      case 36 =>
        new RyoServerMenu1(ryoServerAssist).menu(p)
      case 40 =>
        data.toggleAutoStack(p)
        openCategorySelectMenu(p)
      case 44 =>
        p.getInventory.getContents.foreach(item => {
          if (item != null) {
            if (new NeoStackData(ryoServerAssist).checkItemList(item)) {
              data.addStack(item, p)
              p.getInventory.removeItem(item)
            }
          }
        })
        p.sendMessage(ChatColor.AQUA + "インベントリ内のアイテムをすべてneoStackに収納しました。")
      case _ =>
    }
  }

}

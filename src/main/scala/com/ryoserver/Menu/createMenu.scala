package com.ryoserver.Menu

import com.ryoserver.Inventory.Item.{getEnchantEffectItem, getGachaItem, getItem}
import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.Player.getData
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

object createMenu {

  def menu(p:Player,ryoServerAssist: RyoServerAssist): Inventory = {
    val inv = Bukkit.createInventory(null,54,"りょう鯖メニュー")
    inv.setItem(0,getItem(Material.CRAFTING_TABLE,s"${GREEN}作業台を開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(2,getItem(Material.WOODEN_AXE,s"${GREEN}保護メニューを開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(4,getItem(Material.BOOK,s"${GREEN}クエストを選択します。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(6,getItem(Material.BEACON,s"${GREEN}スキルを選択します。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(8,getItem(Material.NAME_TAG,s"${GREEN}称号一覧を開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(18,getItem(Material.CHEST,s"${GREEN}ストレージを開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(20,getItem(Material.ENDER_CHEST,s"${GREEN}エンダーチェストを開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(22,getItem(Material.SHULKER_BOX,s"${GREEN}ネオスタックを開きます。",List(s"${GRAY}クリックで開きます").asJava))
    inv.setItem(24,getItem(Material.LAVA_BUCKET,s"${GREEN}ゴミ箱を開きます。",List(s"${GRAY}クリックで開きます。",s"${RED}${BOLD}取扱注意！").asJava))
    inv.setItem(26,getItem(Material.FIREWORK_ROCKET,s"${GREEN}ロケット花火を受け取ります。",List(s"${GRAY}クリックで受け取ります。").asJava))
    inv.setItem(36,getItem(Material.CHEST_MINECART,s"${GREEN}運営からのガチャ券を受け取ります。",List(s"${GRAY}クリックで受け取ります。").asJava))
    inv.setItem(38,getGachaItem(Material.PAPER,s"${GREEN}ガチャ券を受け取ります。",List(s"${GRAY}クリックで受け取ります。",
      s"${GRAY}ガチャ券はEXPが100毎に1枚、または",
      s"${GRAY}レベルが10上がる毎に32枚手に入ります。",
      s"${GRAY}受け取れるガチャ券の枚数:" + new getData(ryoServerAssist).getTickets(p) + "枚",
      s"${GRAY}次のガチャ券まであと" + (100 - new getPlayerData(ryoServerAssist).getPlayerExp(p) % 100)).asJava))
    inv.setItem(40,getGachaItem(Material.HONEY_BOTTLE,s"${GREEN}ガチャ特等アイテム交換画面を開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(45,getItem(Material.ENDER_PEARL,s"${GREEN}現在いるワールドのスポーン地点に移動します。",List(s"${GRAY}クリックで移動します。").asJava))
    inv.setItem(46,getItem(Material.COMPASS,s"${GREEN}スポーン地点に移動します。",List(s"${GRAY}クリックで移動します。").asJava))
    inv.setItem(47,getItem(Material.WHITE_BED,s"${GREEN}ホームメニューを開きます。",List(s"${GRAY}クリックで開きます。").asJava))
    inv.setItem(49,getItem(Material.OAK_DOOR,s"${GREEN}ロビーに戻ります。",List(s"${GRAY}クリックでロビーに戻ります。").asJava))
    inv.setItem(51,getItem(Material.FLOWER_BANNER_PATTERN,s"${GREEN}Webサイトのリンクを表示します。",List("クリックで表示します。").asJava))
    inv.setItem(52,getItem(Material.FLOWER_BANNER_PATTERN,s"${GREEN}Dynmapのリンクを表示します。",List("クリックで表示します。").asJava))
    inv.setItem(53,getItem(Material.FLOWER_BANNER_PATTERN,s"${GREEN}投票サイトのリンクを表示します。",List("クリックで表示します。").asJava))
    inv
  }
}

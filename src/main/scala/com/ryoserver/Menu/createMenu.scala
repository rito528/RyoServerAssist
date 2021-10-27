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

object createMenu extends Menu {

  val name = "りょう鯖メニュー"
  val slot = 6

  def menu(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    setItem(1,1,Material.CRAFTING_TABLE,effect = false,s"${GREEN}作業台を開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(3,1,Material.WOODEN_AXE,effect = false,s"${GREEN}保護メニューを開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(5,1,Material.BOOK,effect = false,s"${GREEN}クエストを選択します。",List(s"${GRAY}クリックで開きます。"))
    setItem(7,1,Material.BEACON,effect = false,s"${GREEN}スキルを選択します。",List(s"${GRAY}クリックで開きます。"))
    setItem(9,1,Material.NAME_TAG,effect = false,s"${GREEN}称号一覧を開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(1,3,Material.CHEST,effect = false,s"${GREEN}ストレージを開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(3,3,Material.ENDER_CHEST,effect = false,s"${GREEN}エンダーチェストを開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(5,3,Material.SHULKER_BOX,effect = false,s"${GREEN}ネオスタックを開きます。",List(s"${GRAY}クリックで開きます"))
    setItem(7,3,Material.LAVA_BUCKET,effect = false,s"${GREEN}ゴミ箱を開きます。",List(s"${GRAY}クリックで開きます。",s"${RED}${BOLD}取扱注意！"))
    setItem(9,3,Material.FIREWORK_ROCKET,effect = false,s"${GREEN}ロケット花火を受け取ります。",List(s"${GRAY}クリックで受け取ります。"))
    setItem(1,5,Material.CHEST_MINECART,effect = false,s"${GREEN}運営からのガチャ券を受け取ります。",List(s"${GRAY}クリックで受け取ります。"))
    setItem(3,5,Material.PAPER,effect = true,s"${GREEN}ガチャ券を受け取ります。",List(s"${GRAY}クリックで受け取ります。",
            s"${GRAY}ガチャ券はEXPが100毎に1枚、または",
            s"${GRAY}レベルが10上がる毎に32枚手に入ります。",
            s"${GRAY}受け取れるガチャ券の枚数:" + new getData(ryoServerAssist).getTickets(p) + "枚",
            s"${GRAY}次のガチャ券まであと" + (100 - new getPlayerData(ryoServerAssist).getPlayerExp(p) % 100)))
    setItem(5,5,Material.HONEY_BOTTLE,effect = true,s"${GREEN}ガチャ特等アイテム交換画面を開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(1,6,Material.ENDER_PEARL,effect = false,s"${GREEN}現在いるワールドのスポーン地点に移動します。",List(s"${GRAY}クリックで移動します。"))
    setItem(2,6,Material.COMPASS,effect = false,s"${GREEN}スポーン地点に移動します。",List(s"${GRAY}クリックで移動します。"))
    setItem(3,6,Material.WHITE_BED,effect = false,s"${GREEN}ホームメニューを開きます。",List(s"${GRAY}クリックで開きます。"))
    setItem(5,6,Material.OAK_DOOR,effect = false,s"${GREEN}ロビーに戻ります。",List(s"${GRAY}クリックでロビーに戻ります。"))
    setItem(7,6,Material.FLOWER_BANNER_PATTERN,effect = false,s"${GREEN}Webサイトのリンクを表示します。",List("クリックで表示します。"))
    setItem(8,6,Material.FLOWER_BANNER_PATTERN,effect = false,s"${GREEN}Dynmapサイトのリンクを表示します。",List("クリックで表示します。"))
    setItem(9,6,Material.FLOWER_BANNER_PATTERN,effect = false,s"${GREEN}投票サイトのリンクを表示します。",List("クリックで表示します。"))
    open(p)
  }
}

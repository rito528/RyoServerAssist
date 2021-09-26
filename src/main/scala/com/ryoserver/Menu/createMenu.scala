package com.ryoserver.Menu

import com.ryoserver.Gacha.GachaPaperData
import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.Player.getData
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Material}

import scala.jdk.CollectionConverters._

object createMenu {

  def menu(p:Player,ryoServerAssist: RyoServerAssist): Inventory = {
    val inv = Bukkit.createInventory(null,27,"りょう鯖メニュー")
    inv.setItem(0,getItem(Material.SHULKER_BOX,"運営からのガチャ券を受け取ります。",List("クリックで受け取ります。").asJava))
    inv.setItem(2,getItem(Material.CHEST,"ストレージを開きます。",List("クリックで開きます。").asJava))
    inv.setItem(4,getItem(Material.BOOK,"クエストを選択します。",List("クリックで開きます。").asJava))
    inv.setItem(6,getGachaItem(Material.PAPER,"ガチャ券を受け取ります。",List("クリックで受け取ります。",
      "ガチャ券はEXPが100毎に1枚、または",
      "レベルが10上がる毎に32枚手に入ります。",
      "受け取れるガチャ券の枚数:" + new getData(ryoServerAssist).getTickets(p) + "枚",
      "次のガチャ券まであと" + (100 - new getPlayerData(ryoServerAssist).getPlayerExp(p) % 100)).asJava))
    inv.setItem(8,getItem(Material.BEACON,"スキルを選択します。",List("クリックで開きます。").asJava))
    inv.setItem(9,getItem(Material.WOODEN_AXE,"保護メニューを開きます。",List("クリックで開きます。").asJava))
    inv.setItem(11,getItem(Material.LAVA_BUCKET,"ゴミ箱を開きます。",List("クリックで開きます。","取扱注意！").asJava))
    inv
  }
}

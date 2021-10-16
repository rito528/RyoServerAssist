package com.ryoserver.Stack

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.Stack.PlayerCategory.getSelectedCategory
import com.ryoserver.Stack.PlayerData.playerData
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}
import org.bukkit.ChatColor._

import scala.collection.mutable
import java.util
import scala.jdk.CollectionConverters._

class StackGUI(ryoServerAssist: RyoServerAssist) {

  def openStack(p:Player,page:Int,category:String,isEdit:Boolean): Unit = {
    val data = new StackData(ryoServerAssist)
    val items = data.getSetItems(category)
    val maxPage = items.length / 45 + 1
    var selectPage = page
    if (page >= maxPage) selectPage = maxPage
    val inv = Bukkit.createInventory(null,54,(if (isEdit) "[Edit]" else "") + "stack:" + selectPage)
    var index = 0
    var counter = 0
    val amounts = data.getItemAmount(category,p)
    items.foreach(item => {
      var amount = 0
      val cloneItem = item.clone()
      val meta = item.getItemMeta
      if (amounts.contains(item)) amount = amounts(item)
      if (playerData.contains(p.getUniqueId.toString)) {
      playerData(p.getUniqueId.toString).foreach { case (itemStack, amountData) =>
        if (item.getType == itemStack.getType && item.getItemMeta.getDisplayName == itemStack.getItemMeta.getDisplayName) {
          amount = amountData
        }
      }
      }
      meta.setLore(List(
        s"${BLUE}${BOLD}保有数:${UNDERLINE}${amount}個",
        s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
      ).asJava)
      cloneItem.setItemMeta(meta)
      if (!ItemData.itemData.contains(p.getName)) {
        ItemData.itemData += (p.getName -> mutable.Map(cloneItem -> item))
      } else {
        ItemData.itemData(p.getName) += (cloneItem -> item)
      }
      if (selectPage * 45 >= counter && (selectPage - 1) * 45 <= counter) {
        inv.setItem(index,cloneItem)
        index += 1
      }
      counter += 1
    })
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}前のページに戻ります。",List(s"${GRAY}クリックで戻ります。").asJava))
    if (isEdit) inv.setItem(49,getItem(Material.CHEST,s"${AQUA}アイテムを追加します。",List(s"${GRAY}クリックで追加メニューを開きます。").asJava))
    inv.setItem(53,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}次のページに移動します。",List(s"${GRAY}クリックで移動します。").asJava))
    p.openInventory(inv)
  }

  def openCategorySelectGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,45,"stackカテゴリ選択")
    val lore:java.util.List[String] = new util.ArrayList[String]()
    lore.add(s"${AQUA}左クリックで開きます。")
    if (p.hasPermission("ryoserverassist.stack")) lore.add(s"${RED}右クリックで編集メニューを開きます。")
    inv.setItem(11,getItem(Material.GRASS_BLOCK,s"${GREEN}主要ブロック",lore))
    inv.setItem(13,getItem(Material.OAK_SAPLING,s"${AQUA}主要アイテム",lore))
    inv.setItem(15,getItem(Material.HONEY_BOTTLE,s"${YELLOW}ガチャアイテム",lore))
    inv.setItem(36,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}メニューに戻ります。",List(s"${AQUA}クリックで戻ります。").asJava))
    inv.setItem(40,getItem(Material.HOPPER,s"${WHITE}自動収納を${if (new StackData(ryoServerAssist).isAutoStackEnabled(p)) "off" else "on"}にします。",
      List(s"${AQUA}クリックで切り替えます。",
      s"${WHITE}現在の状態:${if (new StackData(ryoServerAssist).isAutoStackEnabled(p)) s"${GREEN}${BOLD}${UNDERLINE}on" else s"${RED}${BOLD}${UNDERLINE}off"}").asJava))
    inv.setItem(44,getItem(Material.CHEST_MINECART,s"${GREEN}インベントリ内のアイテムをstackに収納します。",List("クリックで収納します。").asJava))
    p.openInventory(inv)
  }

  def openAddGUI(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,"stackアイテム追加メニュー")
    inv.setItem(49,getItem(Material.NETHER_STAR,"クリックでアイテムを追加します。",List("カテゴリ:" + getSelectedCategory(p) + "にアイテムを追加します").asJava))
    p.openInventory(inv)
  }

}

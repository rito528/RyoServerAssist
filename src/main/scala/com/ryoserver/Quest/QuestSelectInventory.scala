package com.ryoserver.Quest

import com.ryoserver.Inventory.Item
import com.ryoserver.RyoServerAssist
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Material}
import org.bukkit.inventory.Inventory

import scala.jdk.CollectionConverters._

class QuestSelectInventory(ryoServerAssist: RyoServerAssist) {

  def selectInventory(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,27,"クエスト選択")
    val lottery = new LotteryQuest(ryoServerAssist)
    val list = lottery.lottery()
    val questType = if (list(1).toString.equalsIgnoreCase("delivery") )"納品クエスト" else ""
    inv.setItem(1,Item.getItem(Material.BOOK,s"[$questType]" + list.head.toString,
      List(
        "アイテムリスト:" +
        list(2).asInstanceOf[List[String]].foreach(item => {
          item
        }),
        "てすと"
      ).asJava))
    p.openInventory(inv)
  }
}

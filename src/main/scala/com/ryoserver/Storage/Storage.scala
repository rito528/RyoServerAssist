package com.ryoserver.Storage

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Sound}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class Storage {
  def save(inv: Inventory, p: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
    var itemList = ""
    inv.getContents.foreach(is => {
      itemList += Item.getStringFromItemStack(is) + ";"
    })
    val storageTable = sql"SELECT UUID FROM Storage WHERE UUID=${p.getUniqueId.toString}"
    if (storageTable.getHeadData.nonEmpty) sql"UPDATE Storage SET invData=$itemList WHERE UUID=${p.getUniqueId.toString}".execute.apply()
    else sql"INSERT INTO Storage(UUID,invData) VALUES (${p.getUniqueId.toString},$itemList)".execute.apply()
  }

  def load(p: Player): Unit = {
    if (p.getRyoServerData.level >= 10) {
      implicit val session: AutoSession.type = AutoSession
      val inv = Bukkit.createInventory(null, 54, "Storage")
      val storageTable = sql"SELECT invData FROM Storage WHERE UUID=${p.getUniqueId.toString}"
      if (storageTable.getHeadData.nonEmpty) {
        storageTable.foreach(rs => {
          rs.string("invData").split(";").zipWithIndex.foreach { case (itemStackString, index) =>
            val itemStack = Item.getItemStackFromString(itemStackString)
            inv.setItem(index, itemStack)
          }
        })
      }
      p.openInventory(inv)
      p.playSound(p.getLocation, Sound.BLOCK_CHEST_OPEN, 1, 1)
    } else {
      p.sendMessage(s"${RED}ストレージ機能はLv.10以上になると使うことができます。")
    }
  }

}

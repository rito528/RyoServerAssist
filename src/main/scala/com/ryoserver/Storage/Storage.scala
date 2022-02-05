package com.ryoserver.Storage

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.util.SQL
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Sound}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class Storage {
  def save(inv: Inventory, p: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
    var itemList = ""
    inv.getContents.foreach(is => {
      val config = new YamlConfiguration
      config.set("i", is)
      itemList += config.saveToString() + ";"
    })
    val storageTable = sql"SELECT UUID FROM Storage WHERE UUID=${p.getUniqueId.toString}"
    if (storageTable.getHeadData.nonEmpty) sql"UPDATE Storage SET invData=$itemList WHERE UUID=${p.getUniqueId.toString}".execute.apply()
    else sql"INSERT INTO Storage(UUID,invData) VALUES (${p.getUniqueId.toString},$itemList)".execute.apply()
  }

  def load(p: Player): Unit = {
    if (p.getQuestLevel >= 10) {
      val sql = new SQL()
      val invData_rs = sql.executeQuery(s"SELECT invData FROM Storage WHERE UUID='${p.getUniqueId.toString}';")
      val inv = Bukkit.createInventory(null, 54, "Storage")
      var counter = 0
      if (invData_rs.next()) {
        val invData = invData_rs.getString("invData").split(";")
        val config = new YamlConfiguration
        invData.foreach(material => {
          config.loadFromString(material)
          inv.setItem(counter, config.getItemStack("i", null))
          counter += 1
        })
      }
      p.openInventory(inv)
      p.playSound(p.getLocation, Sound.BLOCK_CHEST_OPEN, 1, 1)
      sql.close()
    } else {
      p.sendMessage(s"${RED}ストレージ機能はLv.10以上になると使うことができます。")
    }
  }

}

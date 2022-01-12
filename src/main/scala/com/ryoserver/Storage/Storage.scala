package com.ryoserver.Storage

import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Sound}


class Storage(ryoServerAssist: RyoServerAssist) {
  def save(inv: Inventory, p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    var itemList = ""
    inv.getContents.foreach(is => {
      val config = new YamlConfiguration
      config.set("i", is)
      itemList += config.saveToString() + ";"
    })
    val checkRs = sql.executeQuery(s"SELECT UUID FROM Storage WHERE UUID='${p.getUniqueId.toString}'")
    if (checkRs.next()) sql.purseFolder(s"UPDATE Storage SET invData=? WHERE UUID='${p.getUniqueId.toString}'", itemList)
    else sql.purseFolder(s"INSERT INTO Storage(UUID,invData) VALUES ('${p.getUniqueId.toString}',?);", itemList)
    sql.close()
  }

  def load(p: Player): Unit = {
    if (p.getQuestLevel >= 10) {
      val sql = new SQL(ryoServerAssist)
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
    } else {
      p.sendMessage(s"${RED}ストレージ機能はLv.10以上になると使うことができます。")
    }
  }

}

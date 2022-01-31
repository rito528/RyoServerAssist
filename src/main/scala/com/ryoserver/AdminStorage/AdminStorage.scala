package com.ryoserver.AdminStorage

import com.ryoserver.util.SQL
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Sound}

class AdminStorage {

  def save(inv: Inventory): Unit = {
    val sql = new SQL()
    var itemList = ""
    inv.getContents.foreach(is => {
      val config = new YamlConfiguration
      config.set("i", is)
      itemList += config.saveToString() + ";"
    })
    val checkRs = sql.executeQuery(s"SELECT * FROM AdminStorage")
    if (checkRs.next()) sql.purseFolder(s"UPDATE AdminStorage SET invData=?", itemList)
    else sql.purseFolder(s"INSERT INTO AdminStorage(invData) VALUES (?);", itemList)
    sql.close()
  }

  def load(p: Player): Unit = {
    val sql = new SQL()
    val invData_rs = sql.executeQuery(s"SELECT invData FROM AdminStorage;")
    val inv = Bukkit.createInventory(null, 54, "AdminStorage")
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
  }

}

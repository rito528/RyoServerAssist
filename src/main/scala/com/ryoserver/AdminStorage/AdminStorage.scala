package com.ryoserver.AdminStorage

import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.{Bukkit, Sound}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class AdminStorage {

  def save(inv: Inventory): Unit = {
    implicit val session: AutoSession.type = AutoSession
    var itemList = ""
    inv.getContents.foreach(is => {
      val config = new YamlConfiguration
      config.set("i", is)
      itemList += config.saveToString() + ";"
    })
    val adminStorage = sql"SELECT * FROM AdminStorage"
    if (adminStorage.getHeadData.isEmpty) sql"INSERT INTO AdminStorage(invData) VALUES ($itemList)".update.apply()
    else sql"UPDATE AdminStorage SET invData=$itemList".execute.apply()
  }

  def load(p: Player): Unit = {
    val inv = Bukkit.createInventory(null, 54, "AdminStorage")
    implicit val session: AutoSession.type = AutoSession
    val adminStorage = sql"SELECT invData FROM AdminStorage"
    val storageData = adminStorage.getHeadData
    if (storageData.nonEmpty) {
      storageData.get("invData").toString.split(";").zipWithIndex.foreach { case (itemStack, index) =>
        if (itemStack != null) inv.setItem(index, Item.getItemStackFromString(itemStack))
      }
    }
    p.openInventory(inv)
    p.playSound(p.getLocation, Sound.BLOCK_CHEST_OPEN, 1, 1)
  }

}

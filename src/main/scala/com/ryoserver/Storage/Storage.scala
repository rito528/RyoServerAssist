package com.ryoserver.Storage

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory


class Storage(ryoServerAssist: RyoServerAssist) {
  def save(inv: Inventory,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    var itemList = ""
    inv.getContents.foreach(is => {
      val config = new YamlConfiguration
      config.set("i",is)
      itemList += config.saveToString() + ";"
    })
    val checkRs = sql.executeQuery(s"SELECT UUID FROM Storage WHERE UUID='${p.getUniqueId.toString}'")
    if (checkRs.next()) sql.purseFolder(s"UPDATE Storage SET invData=? WHERE UUID='${p.getUniqueId.toString}'",itemList)
    else sql.purseFolder(s"INSERT INTO Storage(UUID,invData) VALUES ('${p.getUniqueId.toString}',?);",itemList)
    sql.close()
  }

  def load(p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val tableCheck_rs = sql.executeQuery("SHOW TABLES LIKE 'Storage';")
    if (!tableCheck_rs.next()) sql.executeSQL("CREATE TABLE Storage(UUID TEXT,invData TEXT);")
    val invData_rs = sql.executeQuery(s"SELECT invData FROM Storage WHERE UUID='${p.getUniqueId.toString}';")
    val inv = Bukkit.createInventory(null,54,"Storage")
    var counter = 0
    if (invData_rs.next()) {
      val invData = invData_rs.getString("invData").split(";")
      val config = new YamlConfiguration
      invData.foreach(material => {
        config.loadFromString(material)
        inv.setItem(counter,config.getItemStack("i",null))
        counter += 1
      })
    }
    p.openInventory(inv)
  }

}
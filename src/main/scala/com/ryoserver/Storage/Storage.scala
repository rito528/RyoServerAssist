package com.ryoserver.Storage

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class Storage(ryoServerAssist: RyoServerAssist) {
  def save(inv: Inventory,p:Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    sql.executeSQL(s"INSERT INTO Storage(UUID,invData) VALUES ('${p.getUniqueId.toString}','${inv.getContents.mkString("", ", ", "")}');")
    sql.close()
  }

  def load(p: Player): Unit = {
    val sql = new SQL(ryoServerAssist)
    val tableCheck_rs = sql.executeQuery("SHOW TABLES LIKE 'Storage';")
    if (!tableCheck_rs.next()) sql.executeSQL("CREATE TABLE Storage(UUID TEXT,invData TEXT);")
    val invData_rs = sql.executeQuery(s"SELECT invData FROM Storage WHERE UUID='${p.getUniqueId.toString}';")
    val inv = Bukkit.createInventory(null,54,"Storage")
    if (invData_rs.next()) {
      val invData = invData_rs.getString("invData")
      //inv.setContents(invData.split(",").toArray[])
    }
    p.openInventory(inv)
  }

}

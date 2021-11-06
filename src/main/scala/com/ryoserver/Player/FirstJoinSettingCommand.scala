package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class FirstJoinSettingCommand(ryoServerAssist: RyoServerAssist) extends CommandExecutor {

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("player")) {
      if (args(0).equalsIgnoreCase("firstJoinItems")) {
        val sql = new SQL(ryoServerAssist)
        sql.executeSQL("CREATE TABLE IF NOT EXISTS firstJoinItems(id INT AUTO_INCREMENT,ItemStack TEXT,PRIMARY KEY(`id`));")
        val items = sql.executeQuery("SELECT ItemStack FROM firstJoinItems;")
        val inv = Bukkit.createInventory(null, 9, "初参加アイテム設定画面")
        var counter = 0
        if (items.next()) {
          val invData = items.getString("ItemStack").split(";")
          val config = new YamlConfiguration
          invData.foreach(material => {
            config.loadFromString(material)
            inv.setItem(counter, config.getItemStack("i", null))
            counter += 1
          })
        }
        sender.asInstanceOf[Player].openInventory(inv)
        return true
      }
    }
    false
  }

}

package com.ryoserver.Stack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: mutable.Map[String, mutable.Map[ItemStack, Int]] = mutable.Map.empty // K - UUID V(K - ItemStack, V - Amount)
  val itemList: mutable.Map[String,ItemStack] = mutable.Map.empty // K - categoryName V - ListItem

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        playerData.foreach{case (uuid,_) =>
          playerData(uuid).foreach{case (itemStack,amount) =>
            val sql = new SQL(ryoServerAssist)
            val config = new YamlConfiguration
            config.set("i",itemStack)
            println(amount)
            val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?",config.saveToString())
            if (check.next()) sql.purseFolder(s"UPDATE StackData SET amount=$amount WHERE UUID='$uuid' AND item=?",config.saveToString())
            else sql.purseFolder(s"INSERT INTO StackData (UUID,category,item,amount) VALUES ('$uuid',(SELECT category FROM StackList WHERE item=?),?,$amount)",config.saveToString())
            sql.close()
          }
        }
        println("saved")
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,20 * 60,20 * 60)
  }

}

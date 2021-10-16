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
  var changedData: mutable.Map[String, Array[ItemStack]] = mutable.Map.empty // K - UUID V(変更が加えられたItemStack)

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    playerData.foreach { case (uuid, _) =>
      playerData(uuid).foreach { case (itemStack, amount) =>
        if (itemStack != null && changedData(uuid).contains(itemStack)) {
          val sql = new SQL(ryoServerAssist)
          val config = new YamlConfiguration
          config.set("i",itemStack)
          val data = new StackData(ryoServerAssist)
          val category = data.getCategory(itemStack)
          val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?", config.saveToString())
          if (check.next()) sql.purseFolder(s"UPDATE StackData SET amount=$amount WHERE UUID='$uuid' AND item=?", config.saveToString())
          else sql.purseFolder(s"INSERT INTO StackData (UUID,category,item,amount) VALUES ('$uuid','$category',?,$amount)", config.saveToString())
          sql.close()
        }
      }
      if (!Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline) {
        playerData = playerData
          .filterNot{case (uuidData,_) => uuidData == uuid}
      }
    }
  }

  def runnableSaver(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist,20 * 60,20 * 60)
  }

}

package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: mutable.Map[String, mutable.Map[ItemStack, Int]] = mutable.Map.empty // K - UUID V(K - ItemStack, V - Amount)
  var changedData: mutable.Map[String, Array[ItemStack]] = mutable.Map.empty // K - UUID V(変更が加えられたItemStack)

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    val sql = new SQL(ryoServerAssist)
    playerData.foreach { case (uuid, _) =>
      playerData(uuid).foreach { case (itemStack, amount) =>
        if (itemStack != null && changedData.contains(uuid) && changedData(uuid).contains(itemStack)) {
          val config = new YamlConfiguration
          config.set("i",itemStack)
          val data = new NeoStackData(ryoServerAssist)
          val category = data.getCategory(itemStack)
          val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?", config.saveToString())
          if (check.next()) sql.purseFolder(s"UPDATE StackData SET amount=$amount WHERE UUID='$uuid' AND item=?", config.saveToString())
          else sql.purseFolder(s"INSERT INTO StackData (UUID,category,item,amount) VALUES ('$uuid','$category',?,$amount)", config.saveToString())
        }
      }
      sql.close()
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

  def loadNeoStackPlayerData(ryoServerAssist: RyoServerAssist,p:Player): Unit = {
    val data = new NeoStackData(ryoServerAssist)
    data.getAllItemAmount(p).foreach{case (is,amount) =>
      if (!playerData.contains(p.getUniqueId.toString)) playerData += (p.getUniqueId.toString -> mutable.Map(is -> amount))
      else playerData(p.getUniqueId.toString) += (is -> amount)
    }
    println(playerData)
  }

}

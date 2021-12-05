package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

object PlayerData {

  var playerData: List[NeoStackDataType] = List.empty
  var changedData: mutable.Map[String, Array[ItemStack]] = mutable.Map.empty // K - UUID V(変更が加えられたItemStack)

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    val sql = new SQL(ryoServerAssist)
    changedData.foreach{case (uuid,array) => {
      array.foreach{itemStack =>
        val data = playerData.filter(playerdata => playerdata.savingItemStack == itemStack).head
        val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.savingItemStack))
        if (check.next()) sql.purseFolder(s"UPDATE StackData SET amount=${data.amount} WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.savingItemStack))
        else sql.purseFolder(s"INSERT INTO StackData (UUID,category,item,amount) VALUES ('$uuid','',?,${data.amount})", Item.getStringFromItemStack(data.savingItemStack))
      }
    }}
    sql.close()
  }

  def runnableSaver(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 20 * 60, 20 * 60)
  }

  def loadNeoStackPlayerData(ryoServerAssist: RyoServerAssist, p: Player): Unit = {
    val uuid = p.getUniqueId.toString
    val gateway = new NeoStackGateway(ryoServerAssist)
    gateway.getPlayerHasNeoStackItems(p).foreach{case (itemStack,data) =>
      playerData :+= NeoStackDataType(uuid,itemStack,null,data.head.toString.toInt)
    }
  }

}

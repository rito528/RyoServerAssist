package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: List[NeoStackDataType] = List.empty
  var changedData: mutable.Map[UUID, Array[ItemStack]] = mutable.Map.empty // K - UUID V - Changed itemstack

  def autoSave(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save(ryoServerAssist)
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 1200, 1200)
  }

  def save(ryoServerAssist: RyoServerAssist): Unit = {
    val sql = new SQL(ryoServerAssist)
    changedData.foreach { case (uuid, array) =>
      array.foreach { itemStack =>
        val data = playerData.filter(playerdata => playerdata.uuid == uuid && playerdata.savingItemStack == itemStack)
        if (data.nonEmpty) {
          val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.head.savingItemStack))
          if (check.next()) sql.purseFolder(s"UPDATE StackData SET amount=${data.head.amount} WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.head.savingItemStack))
          else sql.purseFolder(s"INSERT INTO StackData (UUID,item,amount) VALUES ('$uuid',?,${data.head.amount})", Item.getStringFromItemStack(data.head.savingItemStack))
        }
      }
    }
    sql.close()
  }

  def loadNeoStackPlayerData(ryoServerAssist: RyoServerAssist, p: Player): Unit = {
    /*
     Player data exists check
     The NeoStack data will be loaded the first time you join after startup
     */
    playerData.foreach(data => {
      if (data.uuid == p.getUniqueId) return
    })
    val gateway = new NeoStackGateway(ryoServerAssist)
    gateway.getPlayerHasNeoStackItems(p).foreach(neoStackPlayerData => {
      playerData :+= NeoStackDataType(p.getUniqueId, neoStackPlayerData.itemStack, null, neoStackPlayerData.amount)
    })
  }

}

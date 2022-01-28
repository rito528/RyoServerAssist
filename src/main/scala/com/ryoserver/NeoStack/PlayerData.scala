package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, SQL}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: Set[NeoStackDataType] = Set.empty
  var changedData: mutable.Map[UUID, Array[ItemStack]] = mutable.Map.empty // K - UUID V - Changed itemstack

  def autoSave(ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 1200, 1200)
  }

  def save(): Unit = {
    val sql = new SQL()
    playerData.map(pData => pData.uuid).intersect(changedData.keySet).foreach(uuid =>{
      playerData.map(pData => pData.savingItemStack).intersect(changedData(uuid).toSet).foreach(is => {
        val data = playerData.filter(pData => pData.uuid == uuid && pData.savingItemStack == is).head
        val check = sql.executeQueryPurseFolder(s"SELECT item FROM StackData WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.savingItemStack))
        if (!check.next()) {
          sql.purseFolder(s"INSERT INTO StackData (UUID,item,amount) VALUES ('$uuid',?,${data.amount})", Item.getStringFromItemStack(data.savingItemStack))
        } else {
          sql.purseFolder(s"UPDATE StackData SET amount=${data.amount} WHERE UUID='$uuid' AND item=?", Item.getStringFromItemStack(data.savingItemStack))
        }
      })
    })
    sql.close()
  }

  /*
     NeoStackのプレイヤーデータをロードします。
     これはプレイヤーが参加した際に一度だけ呼び出されます
   */
  def loadNeoStackPlayerData(p: Player): Unit = {
    playerData.foreach(data => {
      if (data.uuid == p.getUniqueId) return
    })
    val gateway = new NeoStackGateway()
    gateway.getPlayerHasNeoStackItems(p).foreach(neoStackPlayerData => {
      playerData += NeoStackDataType(p.getUniqueId, Item.getOneItemStack(neoStackPlayerData.itemStack), null, neoStackPlayerData.amount)
    })
  }

}

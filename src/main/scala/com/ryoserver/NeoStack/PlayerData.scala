package com.ryoserver.NeoStack

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ScalikeJDBC.getData
import com.ryoserver.util.{Item, SQL}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID
import scala.collection.mutable

object PlayerData {

  var playerData: Set[NeoStackDataType] = Set.empty
  var changedData: mutable.Map[UUID, Array[ItemStack]] = mutable.Map.empty // K - UUID V - Changed itemstack

  def autoSave(implicit ryoServerAssist: RyoServerAssist): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 1200, 1200)
  }

  def save(): Unit = {
    playerData.map(pData => pData.uuid).intersect(changedData.keySet).foreach(uuid => {
      playerData.map(pData => pData.savingItemStack).intersect(changedData(uuid).toSet).foreach(is => {
        val data = playerData.filter(pData => pData.uuid == uuid && pData.savingItemStack == is).head
        implicit val session: AutoSession.type = AutoSession
        val stringSavingItemStack = Item.getStringFromItemStack(data.savingItemStack)
        val stackDataTable = sql"SELECT item FROM StackData WHERE UUID=${uuid.toString} AND item=${stringSavingItemStack}"
        if (stackDataTable.getHeadData.isEmpty) {
          sql"INSERT INTO StackData (UUID,item,amount) VALUES (${uuid.toString},$stringSavingItemStack,${data.amount})".execute.apply()
        } else {
          sql"UPDATE StackDATA SET amount=${data.amount} WHERE UUID=${uuid.toString} AND item=$stringSavingItemStack".execute.apply()
        }
      })
    })
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

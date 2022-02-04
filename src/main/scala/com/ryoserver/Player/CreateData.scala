package com.ryoserver.Player

import com.ryoserver.Distribution.DistributionData
import com.ryoserver.SkillSystems.SkillPoint.SkillPointCal
import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Bukkit, Sound}
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class CreateData {

  def createPlayerData(p: Player): Unit = {
    implicit val session: AutoSession.type = AutoSession
    val players = sql"SELECT UUID FROM Players WHERE UUID=${p.getUniqueId.toString};"
    val headData = players.getHeadData
    if (headData.isEmpty) {
      sql"""INSERT INTO Players (UUID,lastLogin,loginDays,consecutiveLoginDays,
        lastDistributionReceived,EXP,Level,questClearTimes,gachaTickets,gachaPullNumber,SkillPoint,SkillOpenPoint,autoStack,VoteNumber)
        VALUES (${p.getUniqueId.toString},NOW(),1,1,(SELECT MAX(id) FROM Distribution),0,0,0,0,0,${new SkillPointCal().getMaxSkillPoint(0)},0,false,0)"""
        .execute.apply()
      val inv = p.getInventory
      val firstJoinItems = sql"SELECT ItemStack FROM firstJoinItems;".map(rs => {
        rs.string("ItemStack")
      }).headOption.apply()
      firstJoinItems match {
        case Some(data) =>
          data.split(";").zipWithIndex.foreach{case (itemStackString,index) =>
            val itemStack = Item.getItemStackFromString(itemStackString)
            if (itemStack != null) inv.setItem(index,itemStack)
          }
        case None =>
      }
      Bukkit.broadcastMessage(s"$AQUA${p.getName}さんが初参加しました！")
      PlayerData.playerData += (p.getUniqueId -> PlayerDataType(0, 0, DistributionData.distributionData.maxBy(_.id).id, new SkillPointCal().getMaxSkillPoint(0), 0, 0, 0, 0, 0, 0, None, 0, 0, 0, None, None, None, autoStack = false, None, None, None))
      Bukkit.getOnlinePlayers.forEach(p => p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1))
    }
  }

}

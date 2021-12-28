package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.scheduler.BukkitRunnable

class SavePlayerData(ryoServerAssist: RyoServerAssist) {

  def autoSave(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 1200)
  }

  def save(): Unit = {
    val sql = new SQL(ryoServerAssist)
    Data.playerData.foreach { case (uuid, data) =>
      sql.executeSQL(s"UPDATE Players SET ${saveDataBuilder(data)} WHERE UUID='${uuid.toString}'")
    }
    sql.close()
  }

  private def saveDataBuilder(playerData: PlayerData): String = {
    val stringBuilder = new StringBuilder
    Map(
      "lastDistributionReceived" -> playerData.lastDistributionReceived,
      "gachaTickets" -> playerData.gachaTickets,
      "SkillPoint" -> playerData.skillPoint,
      "SpecialSkillOpenPoint" -> playerData.specialSkillOpenPoint,
      "OpenedSpecialSkills" -> playerData.OpenedSpecialSkills,
      "OpenedSkills" -> playerData.OpenedSkills,
      "VoteNumber" -> playerData.voteNumber,
      "gachaPullNumber" -> playerData.gachaPullNumber,
      "EXP" -> playerData.exp,
      "Level" -> playerData.level,
      "autoStack" -> playerData.autoStack,
      "OpenedTitles" -> playerData.OpenedTitles,
      "SelectedTitle" -> playerData.SelectedTitle
    ).zipWithIndex.foreach { case ((name, data), index) =>
      if (index != 0) stringBuilder.append(",")
      data match {
        case Some(string: String) =>
          stringBuilder.append(s"$name='$string'")
        case None =>
          stringBuilder.append(s"$name=NULL")
        case _: Int =>
          stringBuilder.append(s"$name=$data")
        case _: Double =>
          stringBuilder.append(s"$name=$data")
        case _: Boolean =>
          stringBuilder.append(s"$name=$data")
        case _ =>
      }
    }
    stringBuilder.toString()
  }

}

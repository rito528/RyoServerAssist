package com.ryoserver.Player

import com.ryoserver.RyoServerAssist
import org.bukkit.scheduler.BukkitRunnable
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class SavePlayerData(implicit ryoServerAssist: RyoServerAssist) {

  def autoSave(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        save()
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 0, 1200)
  }

  def save(): Unit = {
    implicit val session: AutoSession.type = AutoSession
    PlayerData.playerData.foreach { case (uuid, data) =>
      sql"""UPDATE Players SET
           lastDistributionReceived=${data.lastDistributionReceived},
           gachaTickets=${data.gachaTickets},
           SkillPoint=${data.skillPoint},
           SpecialSkillOpenPoint=${data.specialSkillOpenPoint},
           OpenedSpecialSkills=${data.OpenedSpecialSkills},
           OpenedSkills=${data.OpenedSkills},
           VoteNumber=${data.voteNumber},
           gachaPullNumber=${data.gachaPullNumber},
           EXP=${data.exp},
           Level=${data.level},
           autoStack=${data.autoStack},
           OpenedTitles=${data.OpenedTitles},
           SelectedTitle=${data.SelectedTitle}
           WHERE UUID=${uuid.toString}""".execute.apply()
    }
  }

}

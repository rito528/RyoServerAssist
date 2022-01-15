package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Player.PlayerManager.setPlayerData
import org.bukkit.entity.Player

class SkillPointCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "normal" -> addNormalSkillPoint,
      "special" -> addSpecialSkillPoint
    )
  )

  def addNormalSkillPoint(): Unit = {
    sender.asInstanceOf[Player].addSkillOpenPoint(args(1).toInt)
  }

  def addSpecialSkillPoint(): Unit = {
    sender.asInstanceOf[Player].addSpecialSkillOpenPoint(args(1).toInt)
  }

}

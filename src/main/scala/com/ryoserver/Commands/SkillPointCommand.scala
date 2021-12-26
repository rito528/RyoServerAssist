package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Player.RyoServerPlayer
import org.bukkit.entity.Player

class SkillPointCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "normal" -> addNormalSkillPoint,
      "special" -> addSpecialSkillPoint
    )
  )

  def addNormalSkillPoint(): Unit = {
    new RyoServerPlayer(sender.asInstanceOf[Player]).addSkillOpenPoint(args(1).toInt)
  }

  def addSpecialSkillPoint(): Unit = {
    new RyoServerPlayer(sender.asInstanceOf[Player]).addSpecialSkillOpenPoint(args(1).toInt)
  }

}

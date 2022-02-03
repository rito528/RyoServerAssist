package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Player.PlayerManager.setPlayerData
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object SkillPointCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      val args = rawCommandContext.args
      if (args.length != 3) return
      val p = sender.asInstanceOf[Player]
      args.head.toLowerCase match {
        case "normal" =>
          p.addSkillOpenPoint(args(1).toInt)
          p.sendMessage(s"${AQUA}スキルポイントを${args(1)}付与しました。")
        case "special" =>
          p.addSpecialSkillOpenPoint(args(1).toInt)
          p.sendMessage(s"${AQUA}特殊スキルポイントを${args(1)}付与しました。")
      }
    }

    override val args: List[String] = List("normal", "special")

    override val playerCommand: Boolean = true
  })

}

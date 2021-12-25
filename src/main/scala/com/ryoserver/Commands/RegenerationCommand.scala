package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.RyoServerAssist
import com.ryoserver.World.Regeneration.Regeneration
import org.bukkit.{Bukkit, ChatColor}

class RegenerationCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).setNonArgumentExecutor(regeneration)

  private def regeneration(): Unit = {
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドの再生成を行います。")
    Bukkit.broadcastMessage(ChatColor.AQUA + "ラグにご注意ください！")
    new Regeneration(ryoServerAssist).regeneration(true)
  }

}

package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.World.Regeneration.Regeneration
import org.bukkit.Bukkit
import org.bukkit.ChatColor._

class RegenerationCommand() extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).setNonArgumentExecutor(regeneration)

  private def regeneration(): Unit = {
    Bukkit.broadcastMessage(s"${AQUA}ワールドの再生成を行います。")
    Bukkit.broadcastMessage(s"${AQUA}ラグにご注意ください！")
    new Regeneration().regeneration(true)
  }

}

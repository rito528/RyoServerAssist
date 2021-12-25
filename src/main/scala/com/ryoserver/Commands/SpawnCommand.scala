package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SpawnCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).playerCommand()
    .setNonArgumentExecutor(spawn)

  private def spawn(): Unit = {
    sender match {
      case p: Player =>
        p.teleport(Bukkit.getWorld("world").getSpawnLocation)
    }
  }

}

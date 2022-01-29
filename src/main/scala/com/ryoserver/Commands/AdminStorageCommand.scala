package com.ryoserver.Commands

import com.ryoserver.AdminStorage.AdminStorage
import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import org.bukkit.entity.Player

class AdminStorageCommand extends CommandBuilder {
  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map()
  ).setNonArgumentExecutor(openStorage)
    .playerCommand()

  private def openStorage(): Unit = {
    new AdminStorage().load(sender.asInstanceOf[Player])
  }
}

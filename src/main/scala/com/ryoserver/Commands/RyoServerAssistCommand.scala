package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Config.ConfigData
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._

class RyoServerAssistCommand(ryoServerAssist: RyoServerAssist) extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "reload" -> reload
    )
  )

  def reload(): Unit = {
    ConfigData.loadConfig(ryoServerAssist)
    sender.sendMessage(s"${AQUA}configのリロードを行いました。")
  }

}

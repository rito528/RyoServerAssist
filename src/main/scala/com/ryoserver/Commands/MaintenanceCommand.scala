package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Maintenance.MaintenanceData
import org.bukkit.ChatColor._
import org.bukkit.command.TabExecutor

object MaintenanceCommand {

  val executor: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      if (rawCommandContext.args.length != 1) return
      MaintenanceData.setMaintenance(rawCommandContext.args.head.toLowerCase == "on")
      rawCommandContext.sender.sendMessage(s"${AQUA}メンテナンスモードを${if (rawCommandContext.args.head.toLowerCase == "on") "有効" else "無効"}にしました。")
    }

    override val args: List[String] = List("on", "off")
  })

}

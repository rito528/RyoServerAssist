package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StickCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val sender = rawCommandContext.sender
      sender.asInstanceOf[Player].getInventory.addItem(new ItemStack(Material.STICK, 1))
      sender.sendMessage(s"${AQUA}木の棒をインベントリに配布しました。")
    }

    override val args: List[String] = Nil

    override val playerCommand: Boolean = true
  })

}

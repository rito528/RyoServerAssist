package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object HatCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      rawCommandContext.sender match {
        case p: Player =>
          val handItem = p.getInventory.getItemInMainHand
          val headItem = p.getInventory.getHelmet
          if (headItem != null) p.getWorld.dropItemNaturally(p.getLocation(), headItem)
          p.getInventory.remove(handItem)
          p.getInventory.setHelmet(handItem)
      }
    }

    override val args: List[String] = Nil

    override val playerCommand: Boolean = true
  })

}

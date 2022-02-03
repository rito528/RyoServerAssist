package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.Vote.Vote
import org.bukkit.command.TabExecutor

object VoteCommand {

  val executer: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      if (rawCommandContext.args.isEmpty) return
      rawCommandContext.args.head match {
        case "testvote" =>
          new Vote().vote("投票テスト", rawCommandContext.sender.getName)
      }
    }

    override val args: List[String] = List("testVote")
  })

}
